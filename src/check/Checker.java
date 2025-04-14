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
    Board newBoard = new Board(true);

    public Checker(PieceColor pieceColor) {
        this.pieceColor = pieceColor;
        Symbol = (this.pieceColor == PieceColor.WHITE) ? "K" : "k";
        //System.out.println(Symbol);
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

        findKing(board);

        if(King == null){
            System.out.println("못찾음");
        }

        for(int i = 0; i < BOARD_SIZE; i++){
            for(int j = 0; j < BOARD_SIZE; j++){

                Cell now = board.getCell(i, j);

                if(now.getPiece() == null)continue;

                if(now.getPiece().getColor()!= pieceColor){

                    if(now.getPiece().isValidMove(board,now,King)){
                        return true;
                    }

                }
            }
        }

        return false;

    }

    private void initBoard(Board board){
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                newBoard.getCell(i,j).setPiece(board.getCell(i,j).getPiece());
            }
        }
    }

    public boolean canMove(Board board) {

        initBoard(board);
        findKing(board);

        for(int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Cell now = board.getCell(row, col);

                if(now.getPiece() == null) continue;

                if(now.getPiece().getColor() == pieceColor){
                    for(int i=0; i<BOARD_SIZE; i++){
                        for(int j=0; j<BOARD_SIZE; j++){
                            Cell to = board.getCell(i, j);

                            if(to == null){
                                continue;
                            }

                            Piece piece1 = to.getPiece(); // 나중에 deepcopy로 바꿔야함
                            Piece piece2 = now.getPiece();

                            if(now.getPiece().isValidMove(board,now,to)){
                                newBoard.movePiece(now.getRow(),now.getCol(),to.getRow(),to.getCol());
                                if(!isCheck(newBoard)) {
                                    return true;
                                }
                            }
                            newBoard.setPieceTest(i,j,piece1);
                            newBoard.setPieceTest(row,col,piece2);
                        }
                    }
                }
            }
        }
        return false;
    }


}
