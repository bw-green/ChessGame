package piece;

import board.Board;
import board.Cell;
import data.PieceColor;

public class Knight extends Piece {
    public Knight(PieceColor color) {
        super(color);
    }

    // 복사 생성자
    public Knight(Knight other) {
        super(other.getColor());  // Piece 클래스의 복사: enum은 immutable하므로 그대로 사용 가능
    }
    // 복사 메소드
    public Knight deepCopy() {
        return new Knight(this);
    }

    @Override
    public boolean isValidMove(Board board, Cell startCell, Cell endCell) {
        // piece.Knight: 'L'자 이동 (2칸+1칸)
        int rowDiff = Math.abs(endCell.getRow() - startCell.getRow());
        int colDiff = Math.abs(endCell.getCol() - startCell.getCol());
        if ((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2)) {
            Piece dest = endCell.getPiece();
            if (dest == null || dest.getColor() != this.color)
                return true;
        }
        return false;
    }

    @Override
    public String getSymbol() {
        return (color == PieceColor.WHITE) ? "N" : "n";
    }


}
