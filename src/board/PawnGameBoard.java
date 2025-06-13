package board;

import data.PieceColor;
import piece.King;
import piece.Pawn2;
import piece.Piece;

import java.util.Random;

public class PawnGameBoard extends Board    {
//    public PawnGameBoard(boolean canEnpassant, boolean canCastling, boolean canPromotion, boolean initialize){
//        super(false, false, true, initialize);
//    }
    public PawnGameBoard(boolean canEnpassant, boolean canCastling, boolean canPromotion, boolean initialize) {
        super(canEnpassant, canCastling, canPromotion, /*initialize*/ false);
        // initialize == false 이면
        // 빈 보드를 만든 다음에 PawnGameBoard#initializeBoard() 를 호출
        // initialize == true 이면
        // board의 initializeBoard()를 호출해서 원본 보드를 만듦.
        if (initialize) {
            super.initializeBoard();
        }else{
            this.initializeBoard();
        }
    }

    @Override
    public void initializeBoard() {
        // ▶ 폰 게임 전용 로직만 실행
        placeKingsAtOriginalPosition();
        placeRandomPawns();
    }


    public void placeKingsAtOriginalPosition() {
        // 검은색 킹을 cells[0][4]에 놓기
        Cell blackKingCell = getCell(0, 4);
        blackKingCell.setPiece(new King(PieceColor.BLACK));

        // 흰색 킹을 cells[7][4]에 놓기
        Cell whiteKingCell = getCell(7, 4);
        whiteKingCell.setPiece(new King(PieceColor.WHITE));
    }

    public void placeRandomPawns() {
        Random rand = new Random();

        // 흑 폰 배치 (row 0~3)
        for (int col = 0; col < 8; col++) {
            int r;
            do {
                r = rand.nextInt(4); // 0,1,2,3 중 하나
            } while (getPieceAt(r, col) != null);
            getCell(r, col).setPiece(new Pawn2(PieceColor.BLACK));
        }
        // 백 폰 배치 (row 4~7)
        for (int col = 0; col < 8; col++) {
            int r;
            do {
                r = rand.nextInt(4) + 4; // 4,5,6,7 중 하나
            } while (getPieceAt(r, col) != null);
            getCell(r, col).setPiece(new Pawn2(PieceColor.WHITE));
        }

    }

    @Override
    public void Knockback(Cell start, Cell end){
        if(start.getRow()>end.getRow()){
            getCell(start.getRow()+1, start.getCol()).setPiece(start.getPiece());
            getCell(end.getRow()-1, start.getCol()).setPiece(end.getPiece());
            Piece endPiece = getPieceAt(end.getRow()-1, start.getCol());
            if(endPiece instanceof Pawn2){
                Pawn2 endPawn =(Pawn2)endPiece;
                endPawn.isMoved=true;
            }
        }
        else{
            getCell(start.getRow()-1, start.getCol()).setPiece(start.getPiece());
            getCell(end.getRow()+1, start.getCol()).setPiece(end.getPiece());
            Piece endPiece = getPieceAt(end.getRow()+1, start.getCol());
            if(endPiece instanceof Pawn2){
                Pawn2 endPawn =(Pawn2)endPiece;
                endPawn.isMoved=true;
            }
        }

        start.setPiece(null);
        end.setPiece(null);
    }
}
