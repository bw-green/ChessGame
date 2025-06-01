package board;

import piece.*;

public class Chat extends Board {

    public Chat() {
        cells = new Cell[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                cells[row][col] = new Cell(row, col);
            }
        }
        initializeBoard();
    }

    @Override
    public void initializeBoard() {

        cells[0][0].setPiece(PieceFactory.createPieceFromSymbol("r"));
        cells[0][1].setPiece(PieceFactory.createPieceFromSymbol("n")); // Black Knight
        cells[0][4].setPiece(PieceFactory.createPieceFromSymbol("k"));
        cells[0][5].setPiece(PieceFactory.createPieceFromSymbol("q"));

        for (int col = 0; col < 8; col++) {
            // Black Pawn: "p"
            cells[1][col].setPiece(PieceFactory.createPieceFromSymbol("z"));
        }

        // White pieces (아래쪽)
        cells[7][0].setPiece(PieceFactory.createPieceFromSymbol("R")); // White Rook

        cells[5][4].setPiece(PieceFactory.createPieceFromSymbol("K"));

        for (int col = 0; col < 8; col++) {
            // White Pawn: "P"
            cells[6][col].setPiece(PieceFactory.createPieceFromSymbol("Z"));

        }

    }
    @Override
    public void doPromotion(Cell end) {
        if(end.getPiece() instanceof Fawn) {
            end.setPiece(new Mantri(end.getPiece().getColor()));
        }
    }
}
