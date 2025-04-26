package piece;

import board.Board;
import board.Cell;
import data.PieceColor;

public class Bishop extends Piece {
    public Bishop(PieceColor color) {
        super(color);
    }

    // 복사 생성자
    public Bishop(Bishop other) {
        super(other.getColor());  // Piece 클래스의 복사: enum은 immutable하므로 그대로 사용 가능
    }
    // 복사 메소드
    public Bishop deepCopy() {
        return new Bishop(this);
    }

    @Override
    public boolean isValidMove(Board board, Cell start, Cell end) {
        int rowDiff = Math.abs(start.getRow() - end.getRow());
        int colDiff = Math.abs(start.getCol() - end.getCol());

        if (rowDiff == colDiff) { // 대각선 이동
            if (!board.isPathClear(start, end)) {
                return false; // 경로에 기물이 있으면 이동 불가
            }
            Piece dest = end.getPiece();
            if (dest == null || dest.getColor() != this.color) {
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
