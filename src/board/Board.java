package board;

import data.PieceColor;
import piece.*;
import specialRule.SpecialRule;

//////////////////////////////////////////////
public class Board {
    private Cell[][] cells; // 8x8 board.Cell 배열

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
    public void initializeBoard() {
        // Black pieces (위쪽)
        cells[0][0].setPiece(new Rook(PieceColor.BLACK));
        cells[0][1].setPiece(new Knight(PieceColor.BLACK));
        cells[0][2].setPiece(new Bishop(PieceColor.BLACK));
        cells[0][3].setPiece(new Queen(PieceColor.BLACK));
        cells[0][4].setPiece(new King(PieceColor.BLACK));
        cells[0][5].setPiece(new Bishop(PieceColor.BLACK));
        cells[0][6].setPiece(new Knight(PieceColor.BLACK));
        cells[0][7].setPiece(new Rook(PieceColor.BLACK));
        for (int col = 0; col < 8; col++) {
            cells[1][col].setPiece(new Pawn(PieceColor.BLACK));
        }

        // White pieces (아래쪽)
        cells[7][0].setPiece(new Rook(PieceColor.WHITE));
        cells[7][1].setPiece(new Knight(PieceColor.WHITE));
        cells[7][2].setPiece(new Bishop(PieceColor.WHITE));
        cells[7][3].setPiece(new Queen(PieceColor.WHITE));
        cells[7][4].setPiece(new King(PieceColor.WHITE));
        cells[7][5].setPiece(new Bishop(PieceColor.WHITE));
        cells[7][6].setPiece(new Knight(PieceColor.WHITE));
        cells[7][7].setPiece(new Rook(PieceColor.WHITE));
        for (int col = 0; col < 8; col++) {
            cells[6][col].setPiece(new Pawn(PieceColor.WHITE));
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
            if (cell.getPiece() != null) {
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
    public boolean movePiece(int startRow, int startCol, int endRow, int endCol) {
        Cell start = getCell(startRow, startCol);
        Cell end = getCell(endRow, endCol);
        if (start == null || end == null) return false;

        Piece movingPiece = start.getPiece();
        if (movingPiece == null) return false;

        // 1. 이동 가능성 자체 확인
        if (!movingPiece.isValidMove(this, start, end)) return false;

        // 2. 이동하려는 기물이 킹일 경우, 이동 후 위치가 체크 상태인지 검사
        if (movingPiece instanceof King king) {
            Piece targetPieceBackup = end.getPiece(); // 캡처되는 기물이 있다면 임시 저장
            end.setPiece(movingPiece);
            start.setPiece(null);

            boolean isInCheck = isCellUnderAttack(endRow, endCol, king.getColor());

            // 상태 복원
            start.setPiece(movingPiece);
            end.setPiece(targetPieceBackup);

            if (isInCheck) return false; // 체크되는 칸으로는 이동 불가
        }

        // 3. 이동 수행
        end.setPiece(movingPiece);
        start.setPiece(null);
        enPassantChecking();

        if (endRow == 0 || endRow == 7) {
            SpecialRule.promotion(end);
        }
        return true;
    }


    public void enPassantChecking(){
        for(int i = 0 ; i<8; i++){
            for(int j = 0; j<8; j++) {
                Piece enpassantTest = getCell(i, j).getPiece();
                if (enpassantTest instanceof Pawn pawn) {
                    if (pawn.enPassantable && pawn.enPassantCounter == 0) {
                        pawn.enPassantCounter = 1; //한턴은 앙파상을 유지시켜야하므로
                    } else if (pawn.enPassantable && pawn.enPassantCounter == 1) {
                        pawn.enPassantCounter = 0;
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

    //Todo: 기획서에 있는대로 디자인을 바꿔야합니다.
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                sb.append(cells[row][col].toString()).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public Piece getPieceAt(int row, int col) {
        // 0415 update - 좌표에 있는 기물을 받아옴.
        if (row < 0 || row >= 8 || col < 0 || col >= 8) {
            return null; // 보드 범위를 벗어난 경우
        }
        return cells[row][col].getPiece();
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
                        return true;
                    }
                }
            }
        }
        return false;
    }


    // 기물 배치를 통해 test용이용으로 만든 함수.
    // 실제는 사용하지 않아야함
    public void setPieceTest(int row, int col, Piece piece) {
        cells[row][col].setPiece(piece);
    }
}

