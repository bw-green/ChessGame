package check;

import board.Board;
import board.Cell;
import data.PieceColor;
import piece.King;
import piece.Piece;

import java.util.Objects;

// isBlack이 true면 블랙이 아무런 움직임을 할 수 없는 상태인지 false면 화이트가 아무런 움직임을 할 수 없는 상태인지 리턴
public class Checker {    // 백 확인하는거 한개 흑확인하는거 한개로 두개 선언해서 사용할것
    private final PieceColor pieceColor;
    private final String Symbol;
    static int BOARD_SIZE=8;
    public Cell King;
    Board newBoard = new Board(true, true, true,false);
    //체커 현재 많이 아픔
    public Checker(PieceColor pieceColor) {
        this.pieceColor = pieceColor;
        Symbol = (this.pieceColor == PieceColor.WHITE) ? "K" : "k";
        //System.out.println(Symbol);
        newBoard.soutBlock = true;
        if (this.pieceColor == PieceColor.BLACK){
            newBoard.turnChange();
        }
    }

    public void findKing(Board board){
        for(int i = 0; i < BOARD_SIZE; i++){
            for(int j = 0; j < BOARD_SIZE; j++){
                Cell now = board.getCell(i, j);
                if(now.getPiece() == null)continue;
                if(Objects.equals(now.getPiece().getSymbol(), Symbol)){
                    King = now;
                    //System.out.println(Symbol);
                    return;
                }
            }
        }
    }

    public boolean isCheck(Board board) { //isBlack이 true면 블랙이 check 상태인지 false면 화이트가 check인지 리턴
        initBoard(board);
        findKing(board);

        if(King == null){
//            System.out.println("못찾음");  이거 못찾았다로 끝내도 되는건가요?
        }

        for(int i = 0; i < BOARD_SIZE; i++){
            for(int j = 0; j < BOARD_SIZE; j++){

                Cell now = newBoard.getCell(i, j);

                if(now.getPiece() == null)continue;

                if(now.getPiece().getColor()!= pieceColor){

                    if(now.getPiece().isValidMove(newBoard,now,King)){
                        return true;
                    }

                }
            }
        }

        return false;

    }

    private void initBoard(Board board){
        newBoard.canEnpassant = board.canEnpassant;
        newBoard.canCastling = board.canCastling;
        newBoard.canPromotion = board.canPromotion;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {

                if (board.getCell(i,j).getPiece() == null) {
                    newBoard.getCell(i,j).setPiece(null);
                }
                else{
                    newBoard.getCell(i,j).setPiece(board.getCell(i,j).getPiece().deepCopy());
                }

            }
        }
    }

    public boolean canMove(Board board) {
//        System.out.println("진입");
        initBoard(board);
        findKing(board);
        for(int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Cell now = newBoard.getCell(row, col);

                if(now.getPiece() == null) continue;

                if(now.getPiece().getColor() == pieceColor){
                    for(int i=0; i<BOARD_SIZE; i++){
                        for(int j=0; j<BOARD_SIZE; j++){
                            Cell to = newBoard.getCell(i, j);

                            if(to == null){
                                continue;
                            }

                            if(now.getPiece().isValidMove(newBoard,now,to)){

                                newBoard.movePieceTest(now.getRow(),now.getCol(),to.getRow(),to.getCol());

//                                if(now.getRow() == 0 && now.getCol() == 3){
//                                    System.out.println("Board Debug" + to.getRow() + " " + to.getCol());
//                                    System.out.println(newBoard);
//                                }
                                if(!isCheck(newBoard)) {
                                    return true;
                                }
                            }

                            initBoard(board);

                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isOneMoveCheck(Board board, Cell startCell, Cell endCell) {
        initBoard(board);
        findKing(board);
//        System.out.println("진입2");
        Cell now = newBoard.getCell(startCell.getRow(), startCell.getCol());
        Cell to = newBoard.getCell(endCell.getRow(), endCell.getCol());

        if(now.getPiece().isValidMove(newBoard,now,to)){
            newBoard.movePieceTest(now.getRow(),now.getCol(),to.getRow(),to.getCol());
            //System.out.println("newboard.sout : " + newBoard.soutBlock);

            return isCheck(newBoard);

        }

        return false;

    }

}
