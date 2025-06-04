package piece;

import board.Board;
import board.Cell;
import data.PieceColor;

public class Gaza extends Piece{

    public Gaza(PieceColor color) {
        super(color);
    }

    public Gaza(Gaza other) {
        super(other.getColor());
    }

    @Override
    public Gaza deepCopy() {
        return new Gaza(this);
    }

    @Override
    public boolean isValidMove(Board board, Cell startCell, Cell endCell) {
        int startRow = startCell.getRow();
        int startCol = startCell.getCol();
        int endRow = endCell.getRow();
        int endCol = endCell.getCol();
        if(Math.abs(startRow - endRow) == 2 && Math.abs(startCol - endCol) == 2){
            return board.getPieceAt(endRow, endCol) == null || board.getPieceAt(endRow, endCol).getColor() != this.getColor();
        }
        return  false;
    }

    @Override
    public String getSymbol() {
        {
            return (color == PieceColor.WHITE) ? "G" : "g";
        }
    }
}
