package piece;

import board.Board;
import board.Cell;
import data.PieceColor;

//////////////////////////////////////////////
// 4) piece.Piece 추상 클래스 및 구체 기물들

public class King extends Piece {
    public King(PieceColor color) {
        super(color);
    }

    @Override
    public boolean isValidMove(Board board, Cell startCell, Cell endCell) {
        // piece.King: 한 칸씩 이동
        int rowDiff = Math.abs(endCell.getRow() - startCell.getRow());
        int colDiff = Math.abs(endCell.getCol() - startCell.getCol());
        if (rowDiff <= 1 && colDiff <= 1) {
            Piece dest = endCell.getPiece();
            // 도착 칸이 비어있거나 상대 기물이면 가능
            if (dest == null || dest.getColor() != this.color)
                return true;
        }
        return false;
    }

    @Override
    public String getSymbol() {
        return (color == PieceColor.WHITE) ? "K" : "k";
    }
}
