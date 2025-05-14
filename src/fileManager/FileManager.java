package fileManager;

import User.User;
import board.Board;
import board.PieceFactory;
import data.PieceColor;
import data.FileError;
import piece.Piece;
import piece.King;
import piece.Rook;
import piece.Pawn;

import java.io.*;
import java.util.*;
import java.util.Random;

public class FileManager {
    private static final int MAX_SAVES = 5;
    private static final String SAVE_DIR = "saves";
    private static final String USER_DIR = SAVE_DIR + "/users";
    private final String deFault = "No Data"; //기획서 일치
    private final String LSFdeFault = "Last saved file";

    private final ArrayList<String> filename = new ArrayList<>(Collections.nCopies(MAX_SAVES, "NO DATA"));
    private static final ArrayList<Integer> counter = new ArrayList<>(Collections.nCopies(MAX_SAVES, 0));
    private String lastSavedFile = LSFdeFault;
    private int lastSaveFileNum;
    private static int count = 0;
    //moveHistroy, counter, count는 공유되야해서 static으로 선언

    private static FileManager instance = null;

    private FileManager() { //싱글턴 확보
        ensureSaveDirectory();
        loadFileNames();
    }

    public static FileManager getInstance() {
        if (instance == null) {
            instance = new FileManager();
        }
        return instance;
    }

    public ArrayList<String> getFilename() {
        return new ArrayList<>(filename); //복사본 제공
    }

    public String getLastSavedFile() {
        return lastSavedFile;
    }

    public int getLastSaveFileNum() {
        return lastSaveFileNum;
    }

    // 세이브 디렉토리 확인 및 생성
    private void ensureSaveDirectory() {
        File dir = new File(SAVE_DIR);
        if (!dir.exists()) {
            boolean success = dir.mkdirs();
            if (!success) {
                //System.err.println(FileError.FAILED_MAKDIR + SAVE_DIR); //임시 출력본
                throw new IllegalStateException();
            }
        }
    }

    // 유저 저장 디렉토리 확인 및 생성
    private void ensureUserDirectory() {
        File dir = new File(USER_DIR);
        if (!dir.exists()) {
            boolean success = dir.mkdirs();
            if (!success) {
                // 로그 출력이나 에러 코드 사용 가능
                // System.err.println(FileError.FAILED_MAKDIR + USER_DIR);
                throw new IllegalStateException("Failed to create user directory: " + USER_DIR);
            }
        }
    }

    // 세이브 파일 덮어쓰기 (최대 5개 관리, 텍스트 형식)
    public boolean overWriteSavedFile(int slot, Board board) {
        if (slot < 1 || slot > MAX_SAVES) return false;
        slot--;

        if (!new File(SAVE_DIR).exists()) {
            ensureSaveDirectory();
        }

        String saveName = generateRandomSaveName();
        String filePath = getFilePath(slot + 1); //savefile 1~5생성을 위해 +1

        if (board == null) return false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(saveName);
            writer.newLine(); // 두 번째 줄 공백
            writer.newLine();
            writer.write(board.getCurrentTurn() == PieceColor.WHITE ? "WHITE" : "BLACK"); // 세 번째 줄 턴 정보
            writer.newLine();

            // 여기서 보드 상태 직접 저장 (네 번째 줄부터)
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    var piece = board.getCell(row, col).getPiece();
                    writer.write((piece == null ? "." : piece.getSymbol()) + " ");
                }
                writer.newLine();
            }

            // 여기서 특수 룰 상태 저장 시작
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    var piece = board.getCell(row, col).getPiece();
                    if (piece == null) continue;

                    // Pawn
                    if (piece instanceof Pawn pawn) {
                        if (pawn.enPassantable && pawn.enPassantCounter == 1) {
                            writer.write(pawn.getSymbol() + " " + row + " " + col);
                            writer.newLine();
                        }
                    }

                    // King
                    else if (piece instanceof King king) {
                        if (king.firstMove) {
                            writer.write(king.getSymbol() + " " + row + " " + col);
                            writer.newLine();
                        }
                    }

                    // Rook
                    else if (piece instanceof Rook rook) {
                        if (rook.firstMove) {
                            writer.write(rook.getSymbol() + " " + row + " " + col);
                            writer.newLine();
                        }
                    }
                }
            }


            filename.set(slot, saveName);
            counter.set(slot, ++count);
            lastSavedFile = saveName;
            lastSaveFileNum = slot;

            return true;
        } catch (IOException e) {
            //System.out.println(FileError.DEBUG_ERROR_OVERWRITE); //디버깅용
            return false;
        }
    }

    // 세이브 파일 불러오기
    public int loadSavedFile(int slot, Board targetBoard) {
        if (slot < 1 || slot > MAX_SAVES ) return -1;
        if(filename.get(slot-1).equals(deFault)) return 0;
        String filePath = getFilePath(slot);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // 저장 이름
            reader.readLine(); // 공백 줄
            String getLine = reader.readLine(); // 턴 정보 읽기

            if (getLine == null) return -1;

            //턴 정보 읽기(WHITE ? BLACK)
            if (getLine.equalsIgnoreCase("BLACK")) {
                targetBoard.turnChange();
            } else if (!getLine.equalsIgnoreCase("WHITE")) {
                throw new IllegalArgumentException();
            }

            //보드 정보 읽기(8*8)
            for (int row = 0; row < 8; row++) {
                String line = reader.readLine();
                if (line == null) return -1;
                String[] tokens = line.trim().split(" ");
                if (tokens.length != 8) return -1;

                for (int col = 0; col < 8; col++) {
                    String symbol = tokens[col];
                    Piece piece = symbol.equals(".") ? null : PieceFactory.createPieceFromSymbol(symbol);
                    targetBoard.getCell(row, col).setPiece(piece);
                }
            }

            // 특수 룰 정보 읽기
            String specialLine;
            while ((specialLine = reader.readLine()) != null) {
                specialLine = specialLine.trim();
                if (specialLine.isEmpty()) continue;

                String[] parts = specialLine.split(" ");
                if (parts.length != 3) continue; // 잘못된 형식은 무시

                int row = Integer.parseInt(parts[1]);
                int col = Integer.parseInt(parts[2]);

                Piece piece = targetBoard.getCell(row, col).getPiece();
                if (piece == null) continue; // 기물이 없으면 무시

                // 특수 상태 복원
                if (piece instanceof Pawn pawn) {
                    pawn.enPassantable = true;
                    pawn.enPassantCounter = 1;
                }
                else if (piece instanceof King king) {
                    king.firstMove = true;
                }
                else if (piece instanceof Rook rook) {
                    rook.firstMove = true;
                }
            }

            return 1;
        } catch (FileNotFoundException e) {
            // 파일이 아예 없음
            return 0;
        } catch (IOException | IllegalArgumentException e) {
            //System.out.println(FileError.DEBUG_ERROR_LOAD);
            return -1;
        }
    }


    // 세이브 파일 지우기
    public boolean deleteSavedFile(int slot) {
        if (slot < 1 || slot > MAX_SAVES) {
            //System.out.println("세이브 슬롯 번호는 1~5 사이여야 합니다.");
            return false;
        }
        slot--;
        String filePath = getFilePath(slot+1); //savefile 1~5삭제을 위해 +1
        File saveFile = new File(filePath);

        if (!saveFile.exists()) {
            //System.out.println("삭제할 파일이 존재하지 않습니다: 슬롯 " + slot);
            return false;
        }
        if (!filename.get(slot).equals(deFault) &&
                lastSavedFile.equals(filename.get(slot))) { //last saved file update (start)
            int secondMax = -1;
            int secondIndex = -1;

            for (int i = 0; i < MAX_SAVES; i++) {
                if (i == slot) continue;
                if (filename.get(i).equals(deFault)) continue;

                int value = counter.get(i);
                if (value > secondMax) {
                    secondMax = value;
                    secondIndex = i;
                }
            }
            if (secondIndex != -1) {
                lastSavedFile = filename.get(secondIndex);
                lastSaveFileNum = secondIndex;
            } else {
                lastSavedFile = LSFdeFault;
                lastSaveFileNum = -1;
            }
        }   //last saved file update (end)

        if (saveFile.delete()) {
            filename.set(slot, deFault);
            counter.set(slot, 0);
            //System.out.println("세이브 파일이 성공적으로 삭제되었습니다: 슬롯 " + slot);
            return true;
        } else {
            //System.out.println("세이브 파일 삭제에 실패했습니다: 슬롯 " + slot);
            return false;
        }
    }

    // 저장
    public void saveUserList(List<User> users) {

        if (!new File(SAVE_DIR).exists()) {
            ensureSaveDirectory();
        }
        if (!new File(USER_DIR).exists()) {
            ensureUserDirectory();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_DIR + "/userlist.txt"))) {
            for (User user : users) {
                writer.write(user.getId() + "," + user.getPw());
                writer.newLine();
            }
        } catch (IOException e) {
            // System.err.println(FileError.FAILED_TO_SAVE_USER);
            throw new RuntimeException(e);
        }
    }

    // 불러오기
    public List<User> loadUserList() {
        ensureUserDirectory(); // 디렉토리 존재 확인

        File file = new File(USER_DIR + "/userlist.txt");
        List<User> users = new ArrayList<>();

        // 파일이 존재하지 않으면 빈 리스트 반환
        if (!file.exists()) return users;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 공백 제거 및 무의미한 줄 처리
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] tokens = line.split(",", 2); // "," 기준으로 최대 2개 분리
                if (tokens.length != 2) {
                    // 잘못된 형식의 줄은 무시 (혹은 로그 출력 가능)
                    // System.err.println("잘못된 사용자 데이터 형식: " + line);
                    continue;
                }

                String id = tokens[0].trim();
                String pw = tokens[1].trim();

                if (!id.isEmpty() && !pw.isEmpty()) {
                    users.add(new User(id, pw));
                }
            }
        } catch (IOException e) {
            // 에러 출력 (원하시면 FileError.FAILED_TO_LOAD_USER 활용 가능)
            //System.err.println("사용자 파일 로딩 실패: " + e.getMessage());
        }

        return users;
    }







    public void resetTestState() { //Test 제작을 위한 함수 절대 임의로 호출하지 말것!!
        for (int i = 0; i < MAX_SAVES; i++) {
            filename.set(i, "NO DATA");
            counter.set(i, 0);
        }
        count = 0;
        lastSavedFile = LSFdeFault;
        lastSaveFileNum = -1;
    }


    private void loadFileNames() {
        for (int i = 1; i <= MAX_SAVES; i++) {
            String filePath = getFilePath(i);
            File file = new File(filePath);

            if (!file.exists()) {
                filename.set(i - 1, deFault);
                counter.set(i - 1, 0);
                continue;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String saveName = reader.readLine();// 첫 줄
                if (saveName != null && saveName.length() == 10) {
                    filename.set(i - 1, saveName);
                }
            } catch (IOException e) {
                //System.out.println(FileError.DEBUG_ERROR_LOAD_FN); //디버깅용
                //e.printStackTrace(); //디버깅용 후에 주석처리
                //손상된 파일이나 존재하지 않는 파일이나 똑같이 리스트에는 안들어옵니다.
            }
        }
    }

    private String generateRandomSaveName() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random r = new Random();

        while (true) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                sb.append(chars.charAt(r.nextInt(chars.length())));
            }
            String candidate = sb.toString();
            // 중복 검사
            if (!filename.contains(candidate)) {
                return candidate;
            }
            // 중복이면 다시 생성
        }
    }

    //중복 문자열 함수 처리
    private String getFilePath(int slot) {return SAVE_DIR + "/savefile" + slot + ".txt";}
}
