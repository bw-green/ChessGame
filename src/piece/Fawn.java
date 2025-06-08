package piece;

import board.Board;
import board.Cell;
import data.PieceColor;

public class Fawn extends Piece{


    public Fawn(PieceColor color) {
        super(color);
    }

    public Fawn(Fawn other) {
        super(other.getColor());
    }

    @Override
    public Fawn deepCopy() {
        return new Fawn(this);
    }

    @Override
    public boolean isValidMove(Board board, Cell startCell, Cell endCell) {
        int startRow = startCell.getRow();
        int startCol = startCell.getCol();
        int endRow = endCell.getRow();
        int endCol = endCell.getCol();
        Piece dest = endCell.getPiece();
        // 백은 위(-1), 흑은 아래(+1)
        int direction = (color == PieceColor.WHITE) ? -1 : 1;
        // 1) 한 칸 전진 (도착 칸 비어 있어야)
        if (endCol == startCol && endRow == startRow + direction && dest == null)
            return true;

        // 3) 대각선으로 상대 기물을 잡는 이동
        if (Math.abs(endCol - startCol) == 1 && endRow == startRow + direction && dest != null && dest.getColor() != this.color) {
            return true;
        }
        return false;
    }

    @Override
    public String getSymbol() {
        return (color == PieceColor.WHITE) ? "F" : "f";
    }
}
