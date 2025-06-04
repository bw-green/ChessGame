package piece;

import board.Board;
import board.Cell;
import data.PieceColor;
import specialRule.SpecialRule;

public class Pawn2 extends Piece {
    public boolean isMoved = false;
    public Pawn2(PieceColor color) {
        super(color);
    }
    public Pawn2(Pawn2 other) {
        super(other.getColor());  // Piece 클래스의 복사: enum은 immutable하므로 그대로 사용 가능
        this.isMoved = other.isMoved;
    }

    @Override
    public Pawn2 deepCopy() {
        return new Pawn2(this);
    }

    @Override
    public boolean isValidMove(Board board, Cell startCell, Cell endCell) {
        int startRow = startCell.getRow();
        int startCol = startCell.getCol();
        int endRow = endCell.getRow();
        int endCol = endCell.getCol();
        Piece dest = endCell.getPiece();
//        System.out.println("실행됨");
        // 백은 위(-1), 흑은 아래(+1)
        int direction = (color == PieceColor.WHITE) ? -1 : 1;

        // 1) 한 칸 전진 (도착 칸 비어 있어야)
        if (endCol == startCol && endRow == startRow + direction && dest == null)
            return isMoved=true;

        if (endCol == startCol && endRow == startRow + direction && dest != null && dest instanceof Pawn2 & dest.getColor() != color){
            if(startRow - direction >=0 && endRow+direction>=0 &&startRow - direction <8 && endRow+direction<8 ){
                if(board.getPieceAt(startRow- direction, startCol)==null&&board.getPieceAt(endRow + direction, endCol)==null)
                    return isMoved=true;
            }
        }


        // 2) 두 칸 전진 (초기 위치에서만 가능, 중간 칸과 목적지 모두 비어 있어야)
        if (!isMoved && endCol == startCol && endRow == startRow + 2 * direction) {
            Cell intermediate = board.getCell(startRow + direction, startCol);
            if (intermediate.getPiece() == null && dest == null) {
                return isMoved=true;
            }

        }

        // 3) 대각선으로 상대 기물을 잡는 이동
        if (Math.abs(endCol - startCol) == 1 && endRow == startRow + direction && dest != null && dest.getColor() != this.color) {
            return isMoved=true;
        }

        return false;
    }

    @Override
    public String getSymbol() {
        return (color == PieceColor.WHITE) ? "Z" : "z";
    }

}