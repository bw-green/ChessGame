package board;

import piece.*;
import specialRule.SpecialRule;

public class Chaturanga extends Board{



    public Chaturanga(boolean canEnpassant, boolean canCastling, boolean canPromotion, boolean initialize) {
        super(false,false,true,initialize);
    }





    @Override
    public void initializeBoard() {
        // Black pieces (위쪽)
        // 기존: cells[0][0].setPiece(new Rook(PieceColor.BLACK));
        // 변경: PieceFactory.createPieceFromSymbol("r") 사용 ("r"은 Black Rook)
        cells[0][0].setPiece(PieceFactory.createPieceFromSymbol("r"));
        cells[0][1].setPiece(PieceFactory.createPieceFromSymbol("n")); // Black Knight
        cells[0][2].setPiece(PieceFactory.createPieceFromSymbol("g")); // Black Gaza
        cells[0][3].setPiece(PieceFactory.createPieceFromSymbol("k")); // Black matri
        cells[0][4].setPiece(PieceFactory.createPieceFromSymbol("m")); // Black King
        cells[0][5].setPiece(PieceFactory.createPieceFromSymbol("g")); // Black Gaza
        cells[0][6].setPiece(PieceFactory.createPieceFromSymbol("n")); // Black Knight
        cells[0][7].setPiece(PieceFactory.createPieceFromSymbol("r")); // Black Rook

        for (int col = 0; col < 8; col++) {
            // Black Pawn: "p"
            cells[1][col].setPiece(PieceFactory.createPieceFromSymbol("f"));
        }

        // White pieces (아래쪽)
        cells[7][0].setPiece(PieceFactory.createPieceFromSymbol("R")); // White Rook
        cells[7][1].setPiece(PieceFactory.createPieceFromSymbol("N")); // White Knight
        cells[7][2].setPiece(PieceFactory.createPieceFromSymbol("G")); // White Bishop
        cells[7][3].setPiece(PieceFactory.createPieceFromSymbol("K")); // White King
        cells[7][4].setPiece(PieceFactory.createPieceFromSymbol("M")); // White mantri
        cells[7][5].setPiece(PieceFactory.createPieceFromSymbol("G")); // White Bishop
        cells[7][6].setPiece(PieceFactory.createPieceFromSymbol("N")); // White Knight
        cells[7][7].setPiece(PieceFactory.createPieceFromSymbol("R")); // White Rook

        for (int col = 0; col < 8; col++) {
            // White Pawn: "P"
            cells[6][col].setPiece(PieceFactory.createPieceFromSymbol("F"));
        }
        // 나머지 칸은 비어 있음.
    }

    @Override
    public void doPromotion(Cell end) {
        if(end.getPiece() instanceof Fawn) {
            end.setPiece(new Mantri(end.getPiece().getColor()));
            System.out.println("Promotion to mantri");
        }
    }

}
