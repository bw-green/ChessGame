package piece;

import board.Board;
import board.Cell;
import data.PieceColor;

//////////////////////////////////////////////
// 2) board.Cell 클래스: 체스판의 한 칸 (row, col, piece)

public class Rook extends Piece {
    public boolean firstMove = false; // 캐슬링 조건
    public Rook(PieceColor color) {
        super(color);
    }

    // 복사 생성자
    public Rook(Rook other) {
        super(other.getColor());  // Piece 클래스의 복사: enum은 immutable하므로 그대로 사용 가능
        this.firstMove = other.firstMove;
    }
    // 복사 메소드
    public Rook deepCopy() {
        return new Rook(this);
    }

    @Override
    public boolean isValidMove(Board board, Cell start, Cell end) {
        if (start.getRow() == end.getRow() || start.getCol() == end.getCol()) {
            if (!board.isPathClear(start, end)) {
                return false; // 경로에 기물이 있으면 이동 불가
            }
            Piece dest = end.getPiece();
            if (dest == null || dest.getColor() != this.color) {
                firstMove = true; // 룩 이동했으면 firstMove true로 변경
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
