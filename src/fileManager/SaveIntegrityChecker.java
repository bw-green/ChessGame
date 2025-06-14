package fileManager;

import board.Board;
import board.Chaturanga;
import board.PawnGameBoard;
import board.ThreeCheckBoard;
import check.Checker;
import check.GameEnd;
import data.PieceColor;
import piece.*;

import java.util.*;
import java.util.List;

import static gameManager.GameManager.USER_ID;

public class SaveIntegrityChecker {
    private final List<String> lines;
    private Map<String, String> kvMap;
    private List<String> boardLines;
    private List<String> errorList;
    private Board board;

    public SaveIntegrityChecker(List<String> lines) {
        this.lines = lines;
        this.kvMap = new HashMap<>();
        this.boardLines = new ArrayList<>();
        this.errorList = new ArrayList<>();
        this.board = null;
        //validateFile();
    }

    public List<String> getErrors() {
        return errorList;
    }

    /**
     * checkKeyValueBlock
     * 저장된 lines 리스트에서 key-value 블록을 읽어 검사하는 함수.
     * key-value 블록은 "board:" 줄이 나오기 전까지의 줄로 구성됨.
     * 주요 동작:
     * 1. 빈 줄은 건너뜀.
     * 2. "board:" 줄을 만나면 key-value 블록 종료.
     * 3. 각 줄에 대해 형식 검사(checkKeyValueFormat) 수행.
     * 4. key-value 쌍을 kvMap에 저장 (쉼표 제거).
     * 5. key 등장 순서가 올바른지 검사
     * 6. 필수 key 집합 검사 (checkAllowedKeySet).
     * 7. 각 key의 value 유효성 검사 (validateValueByKey).
     * 에러 발생 시 errorList에 메시지 추가 후 false 반환.
     * @return true - 모든 검사가 통과된 경우
     * false - 형식 오류, 순서 오류, 누락 key, 유효하지 않은 value 발생 시
     */
    private boolean checkKeyValueBlock() {
        boolean valid = true;
        kvMap = new HashMap<>(); // key-value 쌍 저장용

        // 키 순서 고려
        List<String> expectedOrder = List.of(
                "id", "save_name", "game_type", "castling", "promotion", "enpassant", "ThreeCheckW", "ThreeCheckB"
        );
        List<String> actualOrder = new ArrayList<>();
        List<Integer> actualOrderLineNums = new ArrayList<>(); // 실제 lines 줄 번호

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            if (line.trim().isEmpty()) continue; // 빈 줄은 건너뜀
            // "board:" 만나면 key-value 블록 종료
            if (line.trim().equalsIgnoreCase("board:")) break;
            // 형식 검사 → 반드시 key:value 형식
            if (!checkKeyValueFormat(line)) {
                errorList.add("Line " + (i + 1) + ": Invalid key-value format");
                valid = false;
            }
            // key, value 분리 (":" 기준 최대 2개 split)
            String[] tokens = line.trim().split(":", 2);
            if (tokens.length != 2) {
                errorList.add("Line " + (i + 1) + ": Invalid key-value format");
                valid = false;
                continue;
            }

            String key = tokens[0].trim();
            String value = tokens[1].trim();
            // 콤마(,)가 있으면 value 분리 및 오류 체크
            int commaIdx = value.indexOf(',');
            if (commaIdx != -1) {
                String afterComma = value.substring(commaIdx + 1).trim();
                if (!afterComma.isEmpty()) {
                    errorList.add("Line " + (i + 1) + ": Invalid value - unexpected text after comma");
                    valid = false;
                }
                value = value.substring(0, commaIdx).trim();
            }
            kvMap.put(key, value);
            actualOrder.add(key);
            actualOrderLineNums.add(i + 1);
        }

        // 순서 검사
        for (int i = 0; i < actualOrder.size(); i++) {
            int lineNum = actualOrderLineNums.get(i);
            if (i >= expectedOrder.size()) {
                errorList.add("Line " + lineNum + ": Invalid extra key - unexpected key '" + actualOrder.get(i) + "'");
                valid = false;
            } else if (!actualOrder.get(i).equals(expectedOrder.get(i))) {
                errorList.add("Line " + lineNum + ": Invalid key order - expected '" + expectedOrder.get(i) + "', found '" + actualOrder.get(i) + "'");
                valid = false;
            }
        }

        // 필수 키가 모두 있는지 검사
        if (!checkAllowedKeySet(kvMap.keySet())) {
            errorList.add("Invalid or missing keys detected in key-value block");
            valid = false;
        }

        // 각 key의 value가 유효한지 검사
        for (String key : kvMap.keySet()) {
            String value = kvMap.get(key);
            if (!validateValueByKey(key, value)) {
                errorList.add("Invalid value for " + key + ": " + value);
                valid = false;
            }
        }

        /*
        세이브파일 속 user가 현재 user_id와 일치하는지 확인
        -> 세이브파일 속 id값과 세이브파일이 들어있는 폴더 이름의 id가 일치하는지 확인
         */
        if(!kvMap.get("id").equals(USER_ID)) valid = false;


        return valid;
    }


    /**
     * findBoardStartIndex
     * <p>
     * 저장된 lines 리스트에서 체스 보드 영역(8x8 형태)의 시작 인덱스를 찾는다.
     * <p>
     * 보드 영역 판별 기준:
     * - 8줄 연속으로 "공백으로 구분된 토큰 8개"로 구성되어 있어야 함 (8x8 보드 상태로 간주).
     * - 각 줄마다 split("\\s+") 결과가 반드시 8개여야 유효한 보드 라인으로 판단.
     * - 첫 유효 보드 영역 발견 시 해당 시작 인덱스를 반환.
     * <p>
     * 예외 처리:
     * - 유효한 보드 영역이 없는 경우 errorList에 메시지 추가 후 -1 반환.
     *
     * @return 보드 시작 인덱스 (0부터 시작), 없으면 -1
     */
    private int findBoardStartIndex() {
        for (int i = 0; i < lines.size() - 7; i++) {
            String[] tokens = lines.get(i).trim().split("\\s+");
            if (tokens.length != 8) continue;

            boolean valid = true;
            for (int j = 1; j < 8; j++) {
                if (i + j >= lines.size()) {
                    valid = false;
                    break;
                }
                String[] nextTokens = lines.get(i + j).trim().split("\\s+");
                if (nextTokens.length != 8) {
                    valid = false;
                    break;
                }
            }

            if (valid) return i; // 연속 8줄이 유효한 보드면 시작점 반환
        }
        errorList.add("Board start not found or board lines malformed.");
        return -1;
    }

    /**
     * checkBoardLines
     * <p>
     * 저장된 lines 리스트에서 보드 영역을 포함한 전체 구성을 검사한다.
     * <p>
     * 검사 순서:
     * 1. "board:" 키워드가 존재하는지 확인
     * 2️. "board:" 바로 아래 줄이 "white" 또는 "black"인지 확인
     * 3. 그 다음 줄부터 8x8 보드 시작 전까지를 특수 좌표로 간주 → 간단한 형식 검사 (3개의 요소인지 확인)
     * 4. 8x8 보드 줄 8줄 검사 → 각 줄은 8개의 요소로 구성되어야 함
     * <p>
     * 검사 실패 시 errorList에 메시지 추가 후 false 반환.
     * 검사 통과 시 true 반환 + boardLines 리스트에 보드 줄 저장.
     *
     * @param start (설계서에서 요구하므로 유지하되, 여기서는 사용하지 않음)
     * @return true - 전체 검사 통과 시
     * false - 검사 실패 시
     */
    private boolean checkBoardLines(int start) {
        boolean valid = true;
        // 1. board: 키워드 찾기
        int boardKeywordIndex = -1;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).trim().equalsIgnoreCase("board:")) {
                boardKeywordIndex = i;
                break;
            }
        }
        if (boardKeywordIndex == -1) {
            errorList.add("\"board:\" keyword not found.");
            valid = false;
        }

        // 2. turn indicator (white / black) 확인
        if (boardKeywordIndex + 1 >= lines.size()) {
            errorList.add("Missing turn indicator after \"board:\"");
            valid = false;
        }
        String turnLine = lines.get(boardKeywordIndex + 1).trim();
        if (!(turnLine.equals("white") || turnLine.equals("black"))) {
            errorList.add("Line " + (boardKeywordIndex + 2) + ": Invalid turn indicator (must be white or black)");
            valid = false;
        }

        // 3. 특수좌표 구간 검사 (8x8 보드 시작 전까지)
        if (start == -1) {
            errorList.add("8x8 board not found.");
            valid = false;
        }

        for (int i = boardKeywordIndex + 2; i < start; i++) {
            String[] tokens = lines.get(i).trim().split("\\s+");
            if (tokens.length != 3) {
                errorList.add("Line " + (i + 1) + ": Invalid special coordinate format (must have 3 elements)");
                valid = false;
            }
            // 여기에서 추가적인 형식 검사 가능 (예: 기물/행/열 형식 등)
        }

        // 4. 8x8 보드 줄 검사
        if (lines.size() < start + 8) {
            errorList.add("Insufficient number of board lines (need 8 lines).");
            valid = false;
        }

        boardLines = new ArrayList<>();
        for (int i = start; i < start + 8; i++) {
            String[] tokens = lines.get(i).trim().split("\\s+");
            if (tokens.length != 8) {
                errorList.add("Line " + (i + 1) + ": Board line must contain 8 elements.");
                valid = false;
            }
            boardLines.add(lines.get(i));
        }

        return valid;
    }

    /**
     * checkKeyValueFormat
     * <p>
     * 주어진 line이 "key:value" 형식으로 구성되어 있는지 검사한다.
     * <p>
     * 검사 기준:
     * - line에 콜론(:)이 반드시 포함되어 있어야 함.
     * - 콜론 기준으로 split했을 때 최대 2개의 항목이어야 함 (key, value).
     * <p>
     * 단순히 key와 value가 존재하는 형식을 확인하는 용도로 사용.
     * key/value의 유효성(내용)은 별도의 validateValueByKey()에서 검사.
     *
     * @param line 검사할 문자열 (한 줄)
     * @return true - "key:value" 형식이면
     * false - 형식이 아니면
     */
    private boolean checkKeyValueFormat(String line) {
        return line.contains(":") && line.split(":", 2).length == 2;
    }

    /**
     * checkAllowedKeySet
     * <p>
     * 주어진 key 집합이 유효한 key 목록과 일치하는지 검사한다.
     * <p>
     * 검사 기준:
     * - 유효한 key 목록 (validKeys)에 정의된 모든 key가 keys 집합에 포함되어 있어야 함.
     * - keys에 불필요한 key가 포함되어 있는지는 검사하지 않음 (필수 key 포함 여부만 검사).
     *
     * @param keys 검사할 key 집합 (key-value 블록에서 추출된 key 목록)
     * @return true - 모든 필수 key가 포함된 경우
     * false - 필수 key 누락 시
     */
    private boolean checkAllowedKeySet(Set<String> keys) {
        Set<String> validKeys = Set.of("id", "save_name", "game_type", "castling", "promotion", "enpassant", "ThreeCheckW", "ThreeCheckB");
        return keys.containsAll(validKeys);
    }

    /**
     * validateValueByKey
     * <p>
     * 주어진 key에 해당하는 value 값이 유효한지 검사한다.
     * key별로 허용되는 value 형식/범위를 기준으로 개별 검증 수행.
     * <p>
     * key별 유효성 기준:
     * - "id", "save_name" : 영문자/숫자만 허용, 길이 1 이상
     * - "game_type"      : 1 ~ 4 중 하나
     * - "castling", "promotion", "enpassant" : "0" 또는 "1"
     * - "ThreeCheckW", "ThreeCheckB" : 정수 -1 ~ 2 범위 내 값
     * <p>
     * 유효하지 않은 key가 들어오는 경우 false 반환.
     *
     * @param key   검사할 key 이름
     * @param value 해당 key에 대응하는 value 문자열
     * @return true - value가 해당 key 기준에 부합하는 경우
     * false - value가 기준에 맞지 않거나 key가 유효하지 않은 경우
     */
    private boolean validateValueByKey(String key, String value) {
        switch (key) {
            case "id":
                return value.matches("[a-zA-Z0-9]+") && value.length() >= 2 && value.length() <= 10;
            case "save_name":
                return value.matches("[a-zA-Z0-9]+") && value.length() == 10;
            case "game_type":
                return value.matches("[1-4]");
            case "castling":
            case "promotion":
            case "enpassant":
                return value.equals("0") || value.equals("1");
            case "ThreeCheckW":
            case "ThreeCheckB":
                try {
                    int v = Integer.parseInt(value);
                    return v >= -1 && v <= 2;
                } catch (NumberFormatException e) {
                    return false;
                }
            default:
                return false;
        }
    }

    /**
     * checkPieceSymbols
     * <p>
     * - 현재 보드에 배치된 기물 기호들이 해당 게임 유형에 따라 허용된 기물에 속하는지를 검사합니다.
     * - 보드 상에 허용된 기물 기호 외의 문자가 존재할 경우 false 반환 + 오류 메시지 추가.
     * - 게임 유형에 따라 허용되는 기물 기호 집합이 다름:
     * - 차투랑가 (gameType=3): K, M, G, N, R, F, k, m, g, n, r, f
     * - 폰게임 (gameType=4): K, Q, R, B, N, Z, k, q, r, b, n, z
     * - 그 외 게임 유형 : K, Q, R, B, N, P, k, q, r, b, n, p
     */
    private boolean checkPieceSymbols() {
        boolean valid = true;
        // 허용 기물 기호 집합 정의
        Set<Character> allowedSymbols;

        // gameType 읽기
        int gameType = Integer.parseInt(kvMap.get("game_type").trim());

        if (gameType == 3) {
            allowedSymbols = new HashSet<>(Arrays.asList('K', 'M', 'G', 'N', 'R', 'F', 'k', 'm', 'g', 'n', 'r', 'f'));
        } else if (gameType == 4) {
            allowedSymbols = new HashSet<>(Arrays.asList('K', 'Q', 'R', 'B', 'N', 'Z', 'k', 'q', 'r', 'b', 'n', 'z'));
        } else {
            allowedSymbols = new HashSet<>(Arrays.asList('K', 'Q', 'R', 'B', 'N', 'P', 'k', 'q', 'r', 'b', 'n', 'p'));
        }

        // boardLines 순회
        for (int i = 0; i < boardLines.size(); i++) {
            String line = boardLines.get(i);
            // 한 줄을 공백 기준으로 나눔
            String[] tokens = line.trim().split("\\s+");

            // 설계서 기준: 한 줄에 8개 토큰이어야 함
            if (tokens.length != 8) {
                errorList.add("Line " + (i + 1) + ": Board line must contain 8 elements.");
                valid = false;
            }

            // 각 토큰 검사
            for (String token : tokens) {
                if (token.equals(".")) {
                    continue; // 빈 칸은 허용
                }
                // 기물 기호는 길이 1의 문자이어야 함
                if (token.length() != 1) {
                    errorList.add("Line " + (i + 1) + ": Invalid piece symbol format: '" + token + "'");
                    valid = false;
                }
                char c = token.charAt(0);
                if (!allowedSymbols.contains(c)) {
                    errorList.add("Line " + (i + 1) + ": Invalid piece symbol: '" + c + "'");
                    valid = false;
                }
            }
        }

        // 모든 검사 통과 → true 반환
        return valid;
    }


    /**
     * checkKingCount
     * <p>
     * - 보드 상에 백의 킹(K)과 흑의 킹(k)이 정확히 1개씩 존재해야만 true를 반환합니다.
     * - 개수가 0개이거나 2개 이상인 경우 false를 반환하고 오류 메시지를 추가합니다.
     * - 이 검사는 게임 유형에 관계없이 항상 적용됩니다.
     */
    private boolean checkKingCount() {
        int whiteKingCount = 0;
        int blackKingCount = 0;

        // boardLines 순회
        for (String line : boardLines) {
            // 각 줄을 문자 단위로 순회
            for (char c : line.replaceAll("\\s+", "").toCharArray()) {
                if (c == 'K') {
                    whiteKingCount++;
                } else if (c == 'k') {
                    blackKingCount++;
                }
            }
        }

        // 킹 개수 검사
        boolean valid = true;
        if (whiteKingCount != 1) {
            errorList.add("Invalid number of white kings (K): " + whiteKingCount);
            valid = false;
        }
        if (blackKingCount != 1) {
            errorList.add("Invalid number of black kings (k): " + blackKingCount);
            valid = false;
        }

        return valid;
    }

    /**
     * checkRuleFlags
     * <p>
     * - castling, promotion, enpassant 항목이 존재하는 경우 그 값이 0 또는 1인지 검사합니다.
     * - gameType이 1(표준 체스)가 아닌 경우에도 이러한 항목이 있는 경우 오류로 간주할 수 있습니다.
     * - 문자열 비교를 통해 유효 값만 허용하며, 허용되지 않은 값이 있으면 false를 반환합니다.
     */
    private boolean checkRuleFlags() {
        boolean valid = true;

        // 검사 대상 키 목록
        String[] ruleKeys = {"castling", "promotion", "enpassant"};

        for (String key : ruleKeys) {
            if (kvMap.containsKey(key)) {
                String value = kvMap.get(key);

                // 값이 "0" 또는 "1"이어야 함
                if (!value.equals("0") && !value.equals("1")) {
                    errorList.add("Invalid value for " + key + ": " + value);
                    valid = false;
                }
            }
        }

        return valid;
    }

    /**
     * checkThreeCheckSettings
     * <p>
     * - ThreeCheckW, ThreeCheckB 항목의 값이 정수이고, -1 이상 2 이하인지 검사합니다.
     * - gameType이 2(쓰리체크)가 아닌데 값이 -1이 아닌 경우에 오류로 간주합니다.
     * - gameType이 2(쓰리체크)일 때 값이 0 이상 2 이하 값이 아닌 경우에도 오류로 간주합니다.
     * - Integer.parseInt() 시 NumberFormatException 발생 시 false 반환합니다.
     */
    private boolean checkThreeCheckSettings() {
        boolean valid = true;

        String[] keys = {"ThreeCheckW", "ThreeCheckB"};
        int gameType = Integer.parseInt(kvMap.get("game_type"));

        for (String key : keys) {
            if (kvMap.containsKey(key)) {
                String valueStr = kvMap.get(key);
                int value;

                try {
                    value = Integer.parseInt(valueStr);
                } catch (NumberFormatException e) {
                    errorList.add("Invalid integer value for " + key + ": " + valueStr);
                    valid = false;
                    continue;
                }

                // 검사 조건
                if (gameType == 2) {
                    // 쓰리체크인 경우: 값은 0 이상 2 이하
                    if (value < 0 || value > 2) {
                        errorList.add("Invalid value for " + key + " in ThreeCheck game: " + value);
                        valid = false;
                    }
                } else {
                    // 쓰리체크가 아닐 때: 값은 반드시 -1
                    if (value != -1) {
                        errorList.add("Invalid value for " + key + " in game_type " + gameType + ": " + value);
                        valid = false;
                    }
                }
            }
        }

        return valid;
    }

    /**
     * checkPieceCoordinates
     * <p>
     * - 캐슬링을 할 수 없는 킹과 룩의 좌표, 앙파상을 당할 수 있는 폰의 좌표가 저장됨.
     * - 저장 형식: "기물기호 col row"
     * - 해당 좌표의 보드 상태와 기록된 기물 기호가 일치하는지 확인.
     * - 허용 기호: "K", "k", "R", "r", "P", "p", "Pf", "pf"
     * - 줄 순서는 보장되지 않음, 각 줄 독립 처리.
     */
    private boolean checkPieceCoordinates() {
        boolean valid = true;

        // "board:" 찾기
        int boardIdx = -1;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).trim().equalsIgnoreCase("board:")) {
                boardIdx = i;
                break;
            }
        }

        if (boardIdx == -1) {
            errorList.add("Missing 'board:' marker in save file.");
            return false;
        }

        // 보드 시작 인덱스 찾기
        int boardStartIdx = findBoardStartIndex();

        // 허용 기호
        Set<String> allowedSymbols = new HashSet<>(Arrays.asList("K", "k", "R", "r", "P", "p", "Pf", "pf","Z", "z"));

        // 특수 좌표 줄 처리 (lines[boardIdx + 2] ~ lines[boardStartIdx - 1])
        // white, black 은 checkBoardLines() 에서 미리 검사
        // 특수좌표 줄 처리 (lines[boardIdx + 2] ~ lines[boardStartIdx - 1])

        for (int i = boardIdx + 2; i < boardStartIdx; i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            String[] tokens = line.split("\\s+");

            // 형식 검증
            if (tokens.length != 3) {
                errorList.add("Line " + (i + 1) + ": Invalid coordinate line format");
                valid = false;
                continue;
            }

            String symbol = tokens[0];
            int col, row;

            // 기호 검증
            if (!allowedSymbols.contains(symbol)) {
                errorList.add("Line " + (i + 1) + ": Invalid piece symbol in coordinates: " + symbol);
                valid = false;
                continue;
            }

            // 좌표 파싱
            try {
                col = Integer.parseInt(tokens[1]);
                row = Integer.parseInt(tokens[2]);
            } catch (NumberFormatException e) {
                errorList.add("Line " + (i + 1) + ": Invalid coordinate value at line");
                valid = false;
                continue;
            }

            // 좌표 범위 확인
            if (row < 0 || row >= 8 || col < 0 || col >= 8) {
                errorList.add("Line " + (i + 1) + ": Coordinate out of bounds at line: (" + col + ", " + row + ")");
                valid = false;
                continue;
            }

            // boardLines 비교
            String[] tokensInRow = boardLines.get(row).split("\\s+");
            String pieceSymbol = tokensInRow[col];

            // Pf/pf는 P/p로 변환 후 비교
            String expectedSymbol = symbol;
            if (symbol.equals("Pf")) expectedSymbol = "P";
            if (symbol.equals("pf")) expectedSymbol = "p";

            // 빈 칸 처리 (기물 없음)
            if (pieceSymbol.equals(".")) {
                errorList.add("Line " + (i + 1) + ": Coordinate points to empty cell at (" + col + ", " + row + "), expected: " + expectedSymbol);
                valid = false;
                continue;
            }

            // 기물 불일치
            if (!pieceSymbol.equals(expectedSymbol)) {
                errorList.add("Line " + (i + 1) + ": Coordinate mismatch at (" + col + ", " + row + "): board has '" + pieceSymbol + "', expected '" + expectedSymbol + "'");
                valid = false;
            }
        }

        return valid;
    }

    /**
     * checkGameEnd
     * - 이미 구현된 GameEnd 클래스를 활용하여 현재 보드가 체크메이트, 스테일메이트, 또는 불충분 기물 상태인지 검사합니다.
     * - Board 객체가 정상적으로 생성된 이후 호출되며, 다음 조건 중 하나라도 true이면 오류로 간주하고 false를 반환합니다:
     * - GameEnd.isCheckMate(board)
     * - GameEnd.isStaleMate(board)
     * - GameEnd.isInsufficientPieces(board)
     * - GameEnd 객체는 현재 턴 정보를 기반으로 생성합니다:
     * - "board:" 바로 다음 줄(lines[boardIdx + 1])에서 currentTurn 정보를 읽어 PieceColor 결정 후 GameEnd 생성.
     * - 모든 조건이 false면 true 반환 (정상 상태).
     */
    private boolean checkGameEnd(Board board) {
        boolean valid = true;

        // "board:" 위치 찾기
        int boardIdx = -1;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).trim().equalsIgnoreCase("board:")) {
                boardIdx = i;
                break;
            }
        }

        if (boardIdx == -1) {
            errorList.add("Missing 'board:' marker in save file.");
            return false;
        }

        // 현재 턴 정보는 boardIdx + 1 번째 줄
        String currentTurnLine = lines.get(boardIdx + 1).trim();

        // 턴 정보 파싱 → PieceColor 결정
        PieceColor currentColor;
        if (currentTurnLine.equalsIgnoreCase("white")) {
            currentColor = PieceColor.WHITE;
        } else if (currentTurnLine.equalsIgnoreCase("black")) {
            currentColor = PieceColor.BLACK;
            board.turnChange(); //작동되는지 확인해야함.
        } else {
            errorList.add("Invalid current turn value: " + currentTurnLine);
            return false; // 턴 정보가 이상하면 검사 실패 처리
        }

        // GameEnd 객체 생성
        GameEnd gameEnd = new GameEnd(currentColor);

        // 검사 1: CheckMate
        if (gameEnd.isCheckMate(board)) {
            errorList.add("Invalid game state: CheckMate detected.");
            valid = false;
        }

        // 검사 2: StaleMate
        if (gameEnd.isStaleMate(board)) {
            errorList.add("Invalid game state: StaleMate detected.");
            valid = false;
        }

        // 검사 3: InsufficientPieces
        if (gameEnd.isInsufficientPieces(board)) {
            errorList.add("Invalid game state: Insufficient pieces detected.");
            valid = false;
        }

        // 검사 4: 킹이 제거되는 상황인지(ex. BLACK King이 Check된 상황에서 WHITE 턴)
        PieceColor otherColor;
        otherColor = currentTurnLine.equalsIgnoreCase("white")
                        ? PieceColor.BLACK: PieceColor.WHITE;
        Checker checker = new Checker(otherColor);
        if(checker.isCheck(board)) {
            errorList.add("GameEnd: King would be eliminated.");
            valid = false;
        }

        return valid;
    }

    public Board validateFile() {
        boolean success = true;

        // 1. 파일 무결성 검사
        if (!checkKeyValueBlock()) success = false;
        int boardStartIdx = findBoardStartIndex();
        if (boardStartIdx == -1) success = false;
        if (!checkBoardLines(boardStartIdx)) success = false;

        // 2. 의미 무결성 검사
        if (!checkPieceSymbols()) success = false;
        if (!checkKingCount()) success = false;
        if (!checkRuleFlags()) success = false;
        if (!checkThreeCheckSettings()) success = false;
        if (!checkPieceCoordinates()) success = false;

        // board 객체를 못 만들 수도 있으므로 success == true인 경우에만 보드 초기화
        if (success) {
            int gameType;
            try {
                gameType = Integer.parseInt(kvMap.get("game_type"));
            } catch (Exception e) {
                errorList.add("Invalid or missing game_type value");
                success = false;
                gameType = -1;
            }
            boolean canEnpassant, canCastling, canPromotion;
            canEnpassant = kvMap.get("enpassant").equalsIgnoreCase("1");
            canCastling = kvMap.get("castling").equalsIgnoreCase("1");
            canPromotion = kvMap.get("promotion").equalsIgnoreCase("1");

            switch (gameType) {
                case 1:
                    board = new Board(canEnpassant, canCastling, canPromotion, false);
                    board.setPieces(boardLines);
                    break;
                case 2:
                    board = new ThreeCheckBoard(canEnpassant,canCastling,canPromotion,false);
                    ThreeCheckBoard thcBoard = (ThreeCheckBoard) board;
                    //thcBoard.setPieces(boardLines);
                    board.setPieces(boardLines);
                    thcBoard.ThreeCheckW = Integer.parseInt(kvMap.get("ThreeCheckW"));
                    thcBoard.ThreeCheckB = Integer.parseInt(kvMap.get("ThreeCheckB"));
                    break;
                case 3:
                    board = new Chaturanga(canEnpassant,canCastling,canPromotion,false);
                    board.setPieces(boardLines);
                    break;
                case 4:
                    board = new PawnGameBoard(canEnpassant, canCastling, canPromotion, true);
                    board.setPieces(boardLines);
                    break;
                default:
                    if (gameType != -1)
                        errorList.add("Invalid game_type value: " + gameType);
                    board = null;
                    success = false;
            }

            //특수좌표 대입 부분
            int boardIdx = -1;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).trim().equalsIgnoreCase("board:")) {
                    boardIdx = i;
                    break;
                }
            }

            for (int i = boardIdx + 2; i < boardStartIdx; i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\s+");
                if (parts.length != 3) continue; // 잘못된 형식은 무시

                int col = Integer.parseInt(parts[1]);
                int row = Integer.parseInt(parts[2]);

                Piece piece = Objects.requireNonNull(board).getCell(row, col).getPiece();
                if (piece == null) continue; // 기물이 없으면 무시

                // 특수 상태 복원
                if (piece instanceof Pawn pawn) {
                    if(parts[0].contains("f")){
                        pawn.isMoved = true;
                    }
                    else{
                        pawn.enPassantable = true;
                        pawn.enPassantCounter = 1;
                    }

                }
                else if (piece instanceof Pawn2 pawn2) {
                    pawn2.isMoved = true;
                }
                else if (piece instanceof King king) {
                    king.firstMove = true;
                }
                else if (piece instanceof Rook rook) {
                    rook.firstMove = true;
                }
            }

            // GameEnd 검사도 board가 생성된 경우만 시도
            if (board != null) {
                if (!checkGameEnd(board)) {
                    success = false;
                }
            }
        } else {
            errorList.add("Skipping additional integrity checks (checkmate/stalemate/insufficient material): previous integrity checks failed.");
            board = null; // 명시적으로 넣어주는 것도 안전
        }

        // 최종 반환
        return success ? board : null;
    }

    /**
     * 테스트 케이스를 위한 메서드
     */
    // 테스트용 public wrapper 메서드들 추가
    public boolean testCheckKeyValueBlock() {
        return checkKeyValueBlock();
    }

    public int testFindBoardStartIndex() { return findBoardStartIndex(); }

    public boolean testCheckBoardLines(int start) {
        return checkBoardLines(start);
    }

    public boolean testCheckPieceSymbols() {
        return checkPieceSymbols();
    }

    public boolean testCheckKingCount() {
        return checkKingCount();
    }

    public boolean testCheckRuleFlags() {
        return checkRuleFlags();
    }

    public boolean testCheckThreeCheckSettings() {
        return checkThreeCheckSettings();
    }

    public boolean testCheckPieceCoordinates() {
        return checkPieceCoordinates();
    }

    // GameEnd는 Board가 필요하므로 파라미터로 넘김
    public boolean testCheckGameEnd(Board board) {
        return checkGameEnd(board);
    }
}

