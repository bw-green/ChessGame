package piece;

import board.Board;
import board.Cell;
import data.PieceColor;

public class Bishop extends Piece {
    public Bishop(PieceColor color) {
        super(color);
    }

    @Override
    public boolean isValidMove(Board board, Cell startCell, Cell endCell) {
        int rowDiff = Math.abs(endCell.getRow() - startCell.getRow());
        int colDiff = Math.abs(endCell.getCol() - startCell.getCol());
        // piece.Bishop: 대각선 이동
        if (rowDiff == colDiff) {
            if (board.isPathClear(startCell, endCell)) {
                Piece dest = endCell.getPiece();
                if (dest == null || dest.getColor() != this.color)
                    return true;
            }
        }
        return false;
    }

    @Override
    public String getSymbol() {
        return (color == PieceColor.WHITE) ? "B" : "b";
    }
}
