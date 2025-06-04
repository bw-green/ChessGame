package board;

import data.PieceColor;
import piece.King;
import piece.Pawn2;
import piece.Piece;

import java.util.Random;

public class PawnGameBoard extends Board    {
    private boolean useDefault; // 설계도엔 누락된, 추가된 부분.
    // 설계도대로 하려면 initialize 값이 true일때 원래 보드를, false일때 폰게임 보드를 호출해야 하는데,
    // 이렇게 하지 않으면 true일때에도 부모의 initializeBoard()에서 오버라이드로 자식 함수를 호출해서 결국 또 폰게임 보드가 생성됨..
    // 사실 폰 게임보드를 호출한 순간부터 initialize 값은 무조건 False로 들어가야만 하는게 맞지만... true일때 원래 보드처럼 초기화한다고 적어놓았기에
    // 이렇게 코딩할 수 밖에 없을 듯 보임.

    public PawnGameBoard(boolean initialize) {
        // canEnpassant=false, canCastling=false, canPromotion=true, initialize=false
        super(false, false, true, initialize);
        this.useDefault = initialize;
    }

    @Override
    public void initializeBoard() {
        if (useDefault) {
            // ▶ 부모의 일반 체스판 초기화 로직을 직접 호출
            super.initializeBoard();
        } else {
            // ▶ 폰 게임 전용 로직만 실행
            placeKingsAtOriginalPosition();
            placeRandomPawns();
        }
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

        // 백 폰 배치 (row 1~4)
        for (int col = 0; col < 8; col++) {
            int r;
            do {
                r = rand.nextInt(4) + 1; // 1,2,3,4 중 하나
            } while (getPieceAt(r, col) != null);
            getCell(r, col).setPiece(new Pawn2(PieceColor.WHITE));
        }
        // 흑 폰 배치 (row 4~7)
        for (int col = 0; col < 8; col++) {
            int r;
            do {
                r = rand.nextInt(4) + 4; // 4,5,6,7 중 하나
            } while (getPieceAt(r, col) != null);
            getCell(r, col).setPiece(new Pawn2(PieceColor.BLACK));
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
