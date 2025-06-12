package check;

import board.Cell;
import board.Board;
import board.ThreeCheckBoard;
import board.Chaturanga;
import data.PieceColor;

public class GameEnd {
    private final PieceColor pieceColor;
    private static int BOARD_SIZE=8;
    private final Checker checker;


    public GameEnd(PieceColor pieceColor) {
        this.pieceColor = pieceColor;
        this.checker = new Checker(this.pieceColor);
    }


    public boolean isCheckMate(Board board) {
        //System.out.printf("%b, %b",checker.isCheck(board),checker.canMove(board));
        return checker.isCheck(board) && !checker.canMove(board);
    }

    public boolean isStaleMate(Board board) {
        //System.out.printf("%b, %b",checker.isCheck(board),checker.canMove(board));
        return !checker.isCheck(board) && !checker.canMove(board);
    }

    public boolean isInsufficientPieces(Board board){
        int count = 0;
        int user1BishopCount = 0;
        int user2BishopCount = 0;
        int whiteBishopCount = 0;
        int blackBishopCount = 0;
        int knightCount = 0;

        for(int row = 0; row < BOARD_SIZE; row++){
            for(int col = 0; col < BOARD_SIZE; col++){
                Cell now = board.getCell(row, col);

                if(now == null || now.getPiece() == null) {
                    continue;
                }

                String nowSymbol = now.getPiece().getSymbol();

                if(nowSymbol.equals("B")){
                    user1BishopCount++;

                    if((row+col)%2 == 0){
                        whiteBishopCount++;
                    }
                    else{
                        blackBishopCount++;
                    }

                }
                else if(nowSymbol.equals("b")){
                    user2BishopCount++;

                    if((row+col)%2 == 0){
                        whiteBishopCount++;
                    }
                    else{
                        blackBishopCount++;
                    }
                }
                else if(nowSymbol.equals("N") || nowSymbol.equals("n")){
                    knightCount++;
                }

                count++;

            }
        }
        if(board instanceof ThreeCheckBoard || board instanceof Chaturanga) {
            return count == 2;
        }

        if(count == 4){
            if (user1BishopCount == 1 && user2BishopCount == 1) {
                return whiteBishopCount == 2 || blackBishopCount == 2;
            }
        }

        else if(count == 3){
            return (whiteBishopCount == 1 || blackBishopCount == 1 || knightCount == 1);
        }

        else return count == 2;

        return false;

    }


}