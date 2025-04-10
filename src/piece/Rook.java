package piece;

import board.Board;
import board.Cell;
import data.PieceColor;

//////////////////////////////////////////////
// 2) board.Cell 클래스: 체스판의 한 칸 (row, col, piece)

public class Rook extends Piece {
    public Rook(PieceColor color) {
        super(color);
    }

    @Override
    public boolean isValidMove(Board board, Cell startCell, Cell endCell) {
        // piece.Rook: 수평 또는 수직 이동
        if (startCell.getRow() == endCell.getRow() || startCell.getCol() == endCell.getCol()) {
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
        return (color == PieceColor.WHITE) ? "R" : "r";
    }
}
