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
    public boolean isValidMove(Board board, Cell startCell, Cell endCell) {
        // piece.Rook: 수평 또는 수직 이동
        if (startCell.getRow() == endCell.getRow() || startCell.getCol() == endCell.getCol()) {
            Piece dest = endCell.getPiece();
            if (dest == null || dest.getColor() != this.color) {
                firstMove = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public String getSymbol() {
        return (color == PieceColor.WHITE) ? "R" : "r";
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

}
