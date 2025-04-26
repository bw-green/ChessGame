package board;

import check.Checker;
import data.MoveErrorType;
import data.MoveResult;
import data.PieceColor;
import data.Unspecified;
import fileManager.FileManager;
import piece.*;
import specialRule.SpecialRule;

//////////////////////////////////////////////
public class Board {
    private Cell[][] cells; // 8x8 board.Cell 배열
    private PieceColor currentTurn = PieceColor.WHITE; // 초기 턴
    public boolean soutBlock = false;

    /**
     * 생성자
     * 8x8 Cell을 생성하고, 초기 기물 배치를 수행.
     */
    public Board() {
        cells = new Cell[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                cells[row][col] = new Cell(row, col);
            }
        }
        initializeBoard();
    }
    public Board(boolean notInitialize){ // test board 임시 -> 각 cell은 null을 가짐
        cells = new Cell[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                cells[row][col] = new Cell(row, col);
            }
        }
    }

    /**
     * 체스판의 초기 기물 배치를 설정합니다.
     * 흑색 기물은 상단(0,1행), 백색 기물은 하단(6,7행)에 배치.
     */
    // 0416 update - PieceFactory 사용하게끔 수정
    public void initializeBoard() {
        // Black pieces (위쪽)
        // 기존: cells[0][0].setPiece(new Rook(PieceColor.BLACK));
        // 변경: PieceFactory.createPieceFromSymbol("r") 사용 ("r"은 Black Rook)
        cells[0][0].setPiece(PieceFactory.createPieceFromSymbol("r"));
        cells[0][1].setPiece(PieceFactory.createPieceFromSymbol("n")); // Black Knight
        cells[0][2].setPiece(PieceFactory.createPieceFromSymbol("b")); // Black Bishop
        cells[0][3].setPiece(PieceFactory.createPieceFromSymbol("q")); // Black Queen
        cells[0][4].setPiece(PieceFactory.createPieceFromSymbol("k")); // Black King
        cells[0][5].setPiece(PieceFactory.createPieceFromSymbol("b")); // Black Bishop
        cells[0][6].setPiece(PieceFactory.createPieceFromSymbol("n")); // Black Knight
        cells[0][7].setPiece(PieceFactory.createPieceFromSymbol("r")); // Black Rook

        for (int col = 0; col < 8; col++) {
            // Black Pawn: "p"
            cells[1][col].setPiece(PieceFactory.createPieceFromSymbol("p"));
        }

        // White pieces (아래쪽)
        cells[7][0].setPiece(PieceFactory.createPieceFromSymbol("R")); // White Rook
        cells[7][1].setPiece(PieceFactory.createPieceFromSymbol("N")); // White Knight
        cells[7][2].setPiece(PieceFactory.createPieceFromSymbol("B")); // White Bishop
        cells[7][3].setPiece(PieceFactory.createPieceFromSymbol("Q")); // White Queen
        cells[7][4].setPiece(PieceFactory.createPieceFromSymbol("K")); // White King
        cells[7][5].setPiece(PieceFactory.createPieceFromSymbol("B")); // White Bishop
        cells[7][6].setPiece(PieceFactory.createPieceFromSymbol("N")); // White Knight
        cells[7][7].setPiece(PieceFactory.createPieceFromSymbol("R")); // White Rook

        for (int col = 0; col < 8; col++) {
            // White Pawn: "P"
            cells[6][col].setPiece(PieceFactory.createPieceFromSymbol("P"));
        }
        // 나머지 칸은 비어 있음.
    }


    /**
     * 주어진 좌표의 board.Cell 객체를 반환합니다.
     * @param row - 행 번호 (입력: int)
     * @param col - 열 번호 (입력: int)
     * @return 해당 위치의 board.Cell (출력: board.Cell)
     */
    public Cell getCell(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8)
            return null;
        return cells[row][col];
    }

    /**
     * 두 board.Cell 사이 경로에 장애물이 없는지 검사.
     * piece.Rook, piece.Bishop, piece.Queen 등의 직선/대각선 이동에 사용.
     * @param start - 시작 board.Cell
     * @param end   - 도착 board.Cell
     * @return true if 경로가 모두 비어있으면, false otherwise
     */
    public boolean isPathClear(Cell start, Cell end) {
        // 0415 update : 필요 없는 기물에 대해서는 함수의 최우선에서 return으로 끝냄.
        if((start == null) || (end == null)) {
            return false;
        }

        Piece piece = start.getPiece();

        if ((piece instanceof Pawn || piece instanceof Knight)) {
            // 폰과 나이트는 중간 경로를 검사할 필요 없음
            return true;
        }

        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        int dRow = endRow - startRow;
        int dCol = endCol - startCol;
        boolean isStraight = dRow == 0 || dCol == 0; // 직선 움직임
        boolean isDiagonal = Math.abs(dRow) == Math.abs(dCol); // 대각 움직임

        if (!isStraight && !isDiagonal) {
            return false; // 비정상 방향은 경로 검사 불필요
        }

        int stepRow = (dRow == 0) ? 0 : dRow / Math.abs(dRow);
        int stepCol = (dCol == 0) ? 0 : dCol / Math.abs(dCol);

        int currentRow = startRow + stepRow;
        int currentCol = startCol + stepCol;

        while (currentRow != endRow || currentCol != endCol) {

            Cell cell = getCell(currentRow, currentCol);

            if (!(0 <= currentRow && currentRow < 8 && 0 <= currentCol && currentCol < 8)){
                //System.out.println(startRow + "," + startCol + "," + endRow + "," + endCol);
                //System.out.println("range is out of bounds");
                break;
            }

            if (cell != null && cell.getPiece() != null) {
                return false;
            }
            currentRow += stepRow;
            currentCol += stepCol;
        }
        return true;
    }


    /**
     * 기물을 이동시키는 메서드.
     * 내부적으로 Piece의 isValidMove()를 통해 이동 가능 여부 판단.
     * @param startRow - 시작 board.Cell 행 번호
     * @param startCol - 시작 board.Cell 열 번호
     * @param endRow   - 도착 board.Cell 행 번호
     * @param endCol   - 도착 board.Cell 열 번호
     * @return true if 이동 성공, false otherwise
     */
    public MoveResult movePiece(int startRow, int startCol, int endRow, int endCol) {
        Cell start = getCell(startRow, startCol);
        Cell end = getCell(endRow, endCol);
        if (start == null || end == null) return MoveResult.FAIL;  // input에서 처리됨

        Piece movingPiece = start.getPiece();
        if (movingPiece == null){
            System.out.println(MoveErrorType.NO_PIECE_AT_START);
            return MoveResult.FAIL;
        }

        // 1. 이동 가능성 자체 확인
        if (movingPiece instanceof King) {
            int colDiff = Math.abs(start.getCol() - end.getCol());
            int rowDiff = Math.abs(start.getRow() - end.getRow());

            if (colDiff == 2 && rowDiff == 0) {
                // 캐슬링 시도 중
                if (!SpecialRule.castling(this, start, end)) {
                    System.out.println(Unspecified.CASTLING_FAILED);
//                    System.out.println("캐슬링 시도중에서 걸림");
                    return MoveResult.FAIL;
                }
            } else {
                // 일반 이동이면 isValidMove 검사
                if (!movingPiece.isValidMove(this, start, end)) {
                    System.out.println(MoveErrorType.INVALID_MOVE_FOR_THIS_PIECE);
//                    System.out.println("킹의 일반 이동에서 걸림");
                    return MoveResult.FAIL;
                }
            }
        } else {
            // 킹이 아닌 경우
            if (!movingPiece.isValidMove(this, start, end)) {
                System.out.println(MoveErrorType.INVALID_MOVE_FOR_THIS_PIECE);
//                System.out.println("일반 기물인데 이동 불가능");
                return MoveResult.FAIL;
            }
        }

        // 2. 의미 오류 검사 추가 (6가지 의미 오류)
        MoveErrorType error = validateMoveMeaning(
                Board.coordinateToNotation(startRow, startCol),
                Board.coordinateToNotation(endRow, endCol),
                currentTurn
        );
        if (error != null){
            System.out.println(error);
//            System.out.println("에러에 걸림");
            return MoveResult.FAIL;
        }

        // 3. 이동하려는 기물이 킹일 경우, 이동 후 위치가 체크 상태인지 검사(isCellunderAttack 함수 제거함)
        if (movingPiece instanceof King king) {
            Piece targetPieceBackup = end.getPiece(); // 캡처되는 기물이 있다면 임시 저장
            end.setPiece(movingPiece);
            start.setPiece(null);

            Checker checker = new Checker(king.getColor());       // 변경
            boolean isInCheck = checker.isCheck(this);            // 변경

            // 상태 복원
            start.setPiece(movingPiece);
            end.setPiece(targetPieceBackup);

            if (isInCheck) {
                System.out.println(MoveErrorType.KING_IS_ATTACK);
                return MoveResult.FAIL;
            } // 체크되는 칸으로는 이동 불가
        }

        // 4. 이동 수행
        end.setPiece(movingPiece);
        start.setPiece(null);
        if(end.getPiece() instanceof Pawn){
            if(((Pawn) end.getPiece()).enPassant){
                getCell(start.getRow(), end.getCol()).setPiece(null);
//                System.out.println("지우기 수행");
            }
        }
        enPassantChecking();

        if (endRow == 0 || endRow == 7) {
            SpecialRule.promotion(end);
        }
        return MoveResult.SUCCESS;
    }

    public void movePieceTest(int startRow, int startCol, int endRow, int endCol) {

        Cell start = getCell(startRow, startCol);
        Cell end = getCell(endRow, endCol);
        if (start == null || end == null) return;  // input에서 처리됨

        Piece movingPiece = start.getPiece();
        if (movingPiece == null){
//            System.out.println(MoveErrorType.NO_PIECE_AT_START);
            return;
        }

        // 1. 이동 가능성 자체 확인
        if (movingPiece instanceof King) {
            int colDiff = Math.abs(start.getCol() - end.getCol());
            int rowDiff = Math.abs(start.getRow() - end.getRow());

            if (colDiff == 2 && rowDiff == 0) {
                // 캐슬링 시도 중
                if (!SpecialRule.castling(this, start, end)) {
//                    System.out.println(Unspecified.CASTLING_FAILED);
//                    System.out.println("캐슬링 시도중에서 걸림");
                    return;
                }
            } else {
                // 일반 이동이면 isValidMove 검사
                if (!movingPiece.isValidMove(this, start, end)) {
//                    System.out.println(MoveErrorType.INVALID_MOVE_FOR_THIS_PIECE);
//                    System.out.println("킹의 일반 이동에서 걸림");
                    return;
                }
            }
        } else {
            // 킹이 아닌 경우
            if (!movingPiece.isValidMove(this, start, end)) {
//                System.out.println(MoveErrorType.INVALID_MOVE_FOR_THIS_PIECE);
//                System.out.println("일반 기물인데 이동 불가능");
                return;
            }
        }

        // 2. 의미 오류 검사 추가 (6가지 의미 오류)
        MoveErrorType error = validateMoveMeaning(
                Board.coordinateToNotation(startRow, startCol),
                Board.coordinateToNotation(endRow, endCol),
                currentTurn
        );
        if (error != null){
            return;
        }

        // 3. 이동하려는 기물이 킹일 경우, 이동 후 위치가 체크 상태인지 검사(isCellunderAttack 함수 제거함)
        if (movingPiece instanceof King king) {
            Piece targetPieceBackup = end.getPiece(); // 캡처되는 기물이 있다면 임시 저장
            end.setPiece(movingPiece);
            start.setPiece(null);

            Checker checker = new Checker(king.getColor());       // 변경
            boolean isInCheck = checker.isCheck(this);            // 변경

            // 상태 복원
            start.setPiece(movingPiece);
            end.setPiece(targetPieceBackup);

            if (isInCheck) {
//                System.out.println(MoveErrorType.KING_IS_ATTACK);
                return;
            } // 체크되는 칸으로는 이동 불가
        }

        // 4. 이동 수행
        end.setPiece(movingPiece);
        start.setPiece(null);
        if(end.getPiece() instanceof Pawn){
            if(((Pawn) end.getPiece()).enPassant){
                getCell(start.getRow(), end.getCol()).setPiece(null);
                System.out.println("앙파상이 제대로 실행됨.");
                System.out.println(this);
//                System.out.println("지우기 수행");
            }
        }
        enPassantChecking();

    }


    public void enPassantChecking(){
        for(int i = 0 ; i<8; i++){
            for(int j = 0; j<8; j++) {
                Piece enpassantTest = getCell(i, j).getPiece();
                if (enpassantTest instanceof Pawn pawn) {
                    if (pawn.enPassantable && pawn.enPassantCounter == 0) {
//                        System.out.println("앙파상카운터 1로 증가");
                        pawn.enPassantCounter = 1; //한턴은 앙파상을 유지시켜야하므로
                    } else if (pawn.enPassantable && pawn.enPassantCounter == 1) {
//                        System.out.println("앙파상 꺼짐");
                        pawn.enPassantCounter = 0;
                        pawn.enPassantable = false;
                    }
                }
            }
        }

    }
    /**
     * 내부 좌표 (row, col)을 체스 표준 표기("a1" ~ "h8")로 변환
     * 예) row=7, col=0 -> "a1"
     *     row=0, col=7 -> "h8"
     */
    public static String coordinateToNotation(int row, int col) {
        // 파일(file): 'a' + col
        char file = (char) ('a' + col);
        // 랭크(rank): 8 - row
        int rank = 8 - row;
        return "" + file + rank;
    }

    /**
     * 체스 표준 표기("a1" ~ "h8")를 내부 좌표 (row, col)로 변환
     * 예) "a1" -> [7, 0]
     *     "h8" -> [0, 7]
     */
    public static int[] notationToCoordinate(String notation) {
        // notation 예: "e2"
        char fileChar = notation.charAt(0);  // 'a' ~ 'h'
        char rankChar = notation.charAt(1);  // '1' ~ '8'

        int col = fileChar - 'a';        // 'a' -> 0, 'b' -> 1, ...
        int rank = rankChar - '0';       // '1' -> 1, '8' -> 8, ...
        int row = 8 - rank;              // row=0이면 rank=8

        return new int[] { row, col };
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("    a b c d e f g h\n");
        sb.append("  ===================\n");
        for (int row = 0; row < 8; row++) {
            int rank = 8 - row;  // 실제 출력되는 행 번호 (8 ~ 1)
            sb.append(rank).append(" | ");
            for (int col = 0; col < 8; col++) {
                sb.append(cells[row][col].toString()).append(" ");
            }
            sb.append("| ").append(rank).append("\n");
        }
        sb.append("  ===================\n");
        sb.append("    a b c d e f g h\n");

        return sb.toString();
    }

    public boolean isCellUnderAttack(int targetRow, int targetCol, PieceColor targetColor) {
        // 0415 update - 특정 좌표가 targetColor의 상대편이 공격중인지 체크함(여기로 이동해도 되는가? 처럼 사용)
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece attacker = cells[r][c].getPiece();

                // Null or 아군이면 스킵
                if (attacker == null || attacker.getColor() == targetColor) continue;

                // 1. Pawn은 특별 처리: 공격 방향만 체크해야 함
                if (attacker instanceof Pawn pawn) {
                    int direction = (pawn.getColor() == PieceColor.WHITE) ? -1 : 1;
                    // 대각선 좌우 두 방향
                    if ((r + direction == targetRow) &&
                            (c - 1 == targetCol || c + 1 == targetCol)) {
                        return true;
                    }
                }
                // 2. 나머지 기물은 일반 canMove() 검사
                else {
                    Cell from = getCell(r, c);
                    Cell to = getCell(targetRow, targetCol);

                    if (attacker.isValidMove(this, from, to)) {
//                        System.out.println(attacker.getSymbol());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 체스 기물 이동 명령에 대해 의미적 오류를 판단하는 메서드입니다.
     *
     * 아래 우선순위에 따라 오류를 판별하며, 가장 중요한 의미 오류 하나만 리턴합니다:
     *
     1. SAME_START_END : 시작과 도착 좌표가 동일
     2. NO_PIECE_AT_START : 출발 위치에 기물이 없음
     3. NOT_YOUR_PIECE : 현재 턴과 다른 색의 기물을 선택
     4. OWN_PIECE_AT_DESTINATION : 도착 좌표에 같은 색 기물이 있음
     5. INVALID_MOVE_FOR_THIS_PIECE : 이동 규칙 자체 위반
     6. PATH_BLOCKED : 직선/대각선 기물의 경로에 장애물 존재
     *
     * @param fromNotation 시작 좌표 (예: "e2")
     * @param toNotation   도착 좌표 (예: "e4")
     * @param currentTurn  현재 턴 색상 (WHITE or BLACK)
     * @return MoveErrorType: 의미 오류가 있으면 그에 해당하는 타입, 없으면 null
     */
    public MoveErrorType validateMoveMeaning(String fromNotation, String toNotation, PieceColor currentTurn) {
        int[] startRC = Board.notationToCoordinate(fromNotation);
        int[] endRC = Board.notationToCoordinate(toNotation);

        int startRow = startRC[0], startCol = startRC[1];
        int endRow = endRC[0], endCol = endRC[1];

        Cell start = getCell(startRow, startCol);
        Cell end = getCell(endRow, endCol);

        Piece movingPiece = (start != null) ? start.getPiece() : null;
        Piece destPiece = (end != null) ? end.getPiece() : null;

        // 1. 시작과 끝이 같은 칸
        if (startRow == endRow && startCol == endCol) {
            return MoveErrorType.SAME_START_END;
        }

        // 2. 출발 칸에 기물이 없음
        if (movingPiece == null) {
            return MoveErrorType.NO_PIECE_AT_START;
        }

        // 3. 현재 턴과 다른 색의 기물
        if (movingPiece.getColor() != currentTurn) {
            return MoveErrorType.NOT_YOUR_PIECE;
        }

        // 4. 도착 칸에 같은 색 기물이 있음
        if (destPiece != null && destPiece.getColor() == currentTurn) {
            return MoveErrorType.OWN_PIECE_AT_DESTINATION;
        }

        // 5. 이동 규칙 위반
        if (movingPiece instanceof King) {
            int rowDiff = Math.abs(start.getRow() - end.getRow());
            int colDiff = Math.abs(start.getCol() - end.getCol());

            if (rowDiff == 0 && colDiff == 2) {
                // 캐슬링 시도 중 → 이동 규칙 위반으로 판단하지 않는다
                // 여기서는 오류를 리턴하지 않고 넘어감
            } else {
                if (!movingPiece.isValidMove(this, start, end)) {
                    return MoveErrorType.INVALID_MOVE_FOR_THIS_PIECE;
                }
            }
        } else {
            if (!movingPiece.isValidMove(this, start, end)) {
//                System.out.println("갑자기 여기서 안됨");
                return MoveErrorType.INVALID_MOVE_FOR_THIS_PIECE;
            }
        }
        return null; // 의미 오류 없음
    }

    public Piece getPieceAt(int row, int col) {
        // 0415 update - 좌표에 있는 기물을 받아옴.
        if (row < 0 || row >= 8 || col < 0 || col >= 8) {
            return null; // 보드 범위를 벗어난 경우
        }
        return cells[row][col].getPiece();
    }

    // 기물 배치를 통해 test용으로 만든 함수.
    // 실제는 사용하지 않아야함
    public void setPieceTest(int row, int col, Piece piece) {
        cells[row][col].setPiece(piece);
    }

    public void turnChange() {
        currentTurn = (currentTurn == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
    }
    public PieceColor getCurrentTurn() {
        return currentTurn;
    }

}

