package piece;

import board.Board;
import board.Cell;
import data.PieceColor;

public class Mantri extends Piece{

    public Mantri(Mantri other) {
        super(other.getColor());
    }


    public Mantri(PieceColor color) {
        super(color);
    }

    @Override
    public Mantri deepCopy() {
        return new Mantri(this);
    }

    public boolean isValidMove(Board board, Cell startCell, Cell endCell) {
        int startRow = startCell.getRow();
        int startCol = startCell.getCol();
        int endRow = endCell.getRow();
        int endCol = endCell.getCol();
        if(Math.abs(startRow - endRow) == 1 && Math.abs(startCol - endCol) == 1){
            return board.getPieceAt(endRow, endCol) == null || board.getPieceAt(endRow, endCol).getColor() != getColor();
        }
        return  false;
    }

    @Override
    public String getSymbol() {
        return (color == PieceColor.WHITE) ? "M" : "m";
    }
}
