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
    public Board(boolean notInitialize){ // test board 임시
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
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        int dRow = endRow - startRow;
        int dCol = endCol - startCol;
        // 이동 방향(증가량)
        int stepRow = (dRow == 0) ? 0 : dRow / Math.abs(dRow);
        int stepCol = (dCol == 0) ? 0 : dCol / Math.abs(dCol);

        int currentRow = startRow + stepRow;
        int currentCol = startCol + stepCol;

        // endCell 직전까지 검사
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
    public boolean movePiece(int startRow, int startCol, int endRow, int endCol) {
        Cell start = getCell(startRow, startCol);
        Cell end = getCell(endRow, endCol);
        if (start == null || end == null)
            return false;

        Piece movingPiece = start.getPiece();
        if (movingPiece == null)
            return false;

        // 기물의 이동 규칙에 따라 이동 가능 여부를 확인
        if (movingPiece.isValidMove(this, start, end)) {
            // 도착 Cell에 기물을 배치하고, 시작 Cell은 비움
            end.setPiece(movingPiece);
            start.setPiece(null);
            enPassantChecking();  // 앙파상에대한 업데이트는 기물이 이동한 후 수행하는 것이 적절합니다.
            if(end.getRow()==7 || end.getRow()==0){
                SpecialRule.promotion(end);//프로모션은 흐름도에 따라 기물 이동을 수행한 후 결정됩니다.
            }

            return true;

        }

        return false;

    }

    public boolean movePieceTest(int startRow, int startCol, int endRow, int endCol) {

        Cell start = getCell(startRow, startCol);
        Cell end = getCell(endRow, endCol);

        if (start == null || end == null)
            return false;

        Piece movingPiece = start.getPiece();
        if (movingPiece == null)
            return false;

        // 기물의 이동 규칙에 따라 이동 가능 여부를 확인
        if (movingPiece.isValidMove(this, start, end)) {
            // 도착 Cell에 기물을 배치하고, 시작 Cell은 비움

            end.setPiece(movingPiece);
            start.setPiece(null);

            // 앙파상에대한 업데이트는 기물이 이동한 후 수행하는 것이 적절합니다.
            return true;
        }

        return false;

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

    // 기물 배치를 통해 test용이용으로 만든 함수 실제는 사용하지 않아야함
    public void setPieceTest(int row, int col, Piece piece) {
        cells[row][col].setPiece(piece);
    }
}
