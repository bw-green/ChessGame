package piece;

import board.Board;
import board.Cell;
import data.PieceColor;
import specialRule.SpecialRule;

public class Pawn extends Piece {
    public boolean enPassantable;
    public int enPassantCounter;
    public Pawn(PieceColor color) {
        super(color);
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
        // 초기 2칸 이동을 위한 시작 행
        int startRowForDouble = (color == PieceColor.WHITE) ? 6 : 1;

        // 1) 한 칸 전진 (도착 칸 비어 있어야)
        if (endCol == startCol && endRow == startRow + direction && dest == null)
            return true;

        // 2) 두 칸 전진 (초기 위치에서만 가능, 중간 칸과 목적지 모두 비어 있어야)
        if (startRow == startRowForDouble && endCol == startCol && endRow == startRow + 2 * direction) {
            Cell intermediate = board.getCell(startRow + direction, startCol);
            if (intermediate.getPiece() == null && dest == null){
                enPassantable = true;
                return true;
            }

        }

        // 3) 대각선으로 상대 기물을 잡는 이동
        if (Math.abs(endCol - startCol) == 1 && endRow == startRow + direction && dest != null && dest.getColor() != this.color) {
            return true;
        }

        // 4) 대각선으로 이동하려는 시도인데, 기물이 없을 때. 앙파상 의심
        if (Math.abs(endCol - startCol) == 1 && endRow == startRow + direction && dest == null) {
            Cell enPassantCell = board.getCell(startRow,endCol); //앙파상이었을 경우를 가정하여 그 셀을 가져온다.
            Pawn enPassantPiece = (Pawn) enPassantCell.getPiece(); // 그 셀에서 기물을 받고 폰으로 변환한다.
            if(enPassantPiece!=null && enPassantPiece.enPassantable && enPassantCounter==1) // null이 아니고, enPassatable이 true라면,
                return SpecialRule.enPassant(startCell,endCell, enPassantCell);
        }

        return false;
    }

    @Override
    public String getSymbol() {
        return (color == PieceColor.WHITE) ? "P" : "p";
    }
}
