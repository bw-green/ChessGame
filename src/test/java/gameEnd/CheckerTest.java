package test.java.gameEnd;

import board.Board;
import check.Checker;
import data.PieceColor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import piece.King;
import piece.Pawn;
import piece.Queen;
import piece.Rook;

public class CheckerTest {

    Checker checker;
    Board board;
    @Test
    @DisplayName("findKing Test")
    public void findKingTest() {
        board = new Board(true);
        Checker whiteChecker = new Checker(PieceColor.WHITE);
        Checker blackChecker = new Checker(PieceColor.BLACK);

        board.setPieceTest(0,0,new King(PieceColor.WHITE));
        board.setPieceTest(7,7,new King(PieceColor.BLACK));

        System.out.println(board);

        whiteChecker.findKing(board);
        blackChecker.findKing(board);
        System.out.println(whiteChecker.King.getRow()+" "+whiteChecker.King.getCol());
        System.out.println(blackChecker.King.getRow() +" "+ blackChecker.King.getCol());

        board = new Board(true);

        board.setPieceTest(5,5,new King(PieceColor.WHITE));
        board.setPieceTest(4,4,new King(PieceColor.BLACK));

        System.out.println(board);
        whiteChecker.findKing(board);
        blackChecker.findKing(board);
        System.out.println(whiteChecker.King.getRow()+" "+whiteChecker.King.getCol());
        System.out.println(blackChecker.King.getRow() +" "+ blackChecker.King.getCol());


        board = new Board(true);

        board.setPieceTest(7,0,new King(PieceColor.WHITE));
        board.setPieceTest(0,7,new King(PieceColor.BLACK));
        whiteChecker.findKing(board);
        blackChecker.findKing(board);
        System.out.println(board);
        System.out.println(whiteChecker.King.getRow()+" "+whiteChecker.King.getCol());
        System.out.println(blackChecker.King.getRow() +" "+ blackChecker.King.getCol());
    }




    @Test
    @DisplayName("isCheck 테스트")
    public void isCheckTest() {
        int cnt = 1;
        System.out.println("Test"+ (cnt++) +"---------------");
        board = new Board(true);
        board.setPieceTest(0,6,new King(PieceColor.WHITE));
        board.setPieceTest(2,6,new Queen(PieceColor.BLACK));
        board.setPieceTest(7,7,new King(PieceColor.BLACK));
        Checker whiteChecker = new Checker(PieceColor.WHITE);
        Checker blackChecker = new Checker(PieceColor.BLACK);
        System.out.println(board);
        System.out.println("white isCheck is "+whiteChecker.isCheck(board));
        System.out.println("black isCheck is " + blackChecker.isCheck(board));

        System.out.println("Test"+ (cnt++) +"---------------");
        board = new Board(true);
        board.setPieceTest(0,7,new King(PieceColor.WHITE));
        board.setPieceTest(0,0,new Queen(PieceColor.BLACK));
        board.setPieceTest(7,7,new King(PieceColor.BLACK));
        board.setPieceTest(7,0,new Queen(PieceColor.WHITE));
        System.out.println(board);
        System.out.println("white isCheck is "+whiteChecker.isCheck(board));
        System.out.println("black isCheck is " + blackChecker.isCheck(board));

        System.out.println("Test"+ (cnt++) +"---------------");
        board = new Board(true);
        board.setPieceTest(0,7,new King(PieceColor.WHITE));
        board.setPieceTest(0,6,new Rook(PieceColor.BLACK));
        board.setPieceTest(7,7,new King(PieceColor.BLACK));
        System.out.println(board);
        System.out.println("white isCheck is "+whiteChecker.isCheck(board));
        System.out.println("black isCheck is " + blackChecker.isCheck(board));

        System.out.println("Test"+ (cnt++) +"---------------");
        board = new Board(true);
        board.setPieceTest(7,4,new King(PieceColor.WHITE));
        board.setPieceTest(7,1,new Rook(PieceColor.BLACK));
        board.setPieceTest(6,2,new Queen(PieceColor.BLACK));
        board.setPieceTest(0,0,new King(PieceColor.BLACK));
        System.out.println(board);
        System.out.println("white isCheck is "+whiteChecker.isCheck(board));
        System.out.println("black isCheck is " + blackChecker.isCheck(board));
    }

    @Test
    @DisplayName("canMove 테스트")
    public void canMoveTest() {
        int cnt = 1;
        System.out.println("Test"+ (cnt++) +"---------------");
        board = new Board(true);
        Checker whiteChecker = new Checker(PieceColor.WHITE);
        Checker blackChecker = new Checker(PieceColor.BLACK);
        board.setPieceTest(0,6,new King(PieceColor.WHITE));
        board.setPieceTest(2,6,new Queen(PieceColor.BLACK));
        board.setPieceTest(7,7,new King(PieceColor.BLACK));
        System.out.println(board);
        System.out.println("white : "+whiteChecker.canMove(board));
        System.out.println("black : "+blackChecker.canMove(board));
        System.out.println();


        System.out.println("Test"+ (cnt++) +"---------------");
        board = new Board(true);
        board.setPieceTest(0,7,new King(PieceColor.WHITE));
        board.setPieceTest(2,6,new Queen(PieceColor.BLACK));
        board.setPieceTest(7,7,new King(PieceColor.BLACK));
        System.out.println(board);
        System.out.println(whiteChecker.canMove(board));
        System.out.println(blackChecker.canMove(board));
        System.out.println();
        System.out.println("Test"+ (cnt++) +"---------------");
        board = new Board(true);
        board.setPieceTest(0,7,new King(PieceColor.BLACK));
        board.setPieceTest(2,6,new Queen(PieceColor.WHITE));
        board.setPieceTest(7,7,new King(PieceColor.WHITE));
        System.out.println(board);
        System.out.println("white : "+whiteChecker.canMove(board));
        System.out.println("black : "+blackChecker.canMove(board));
        System.out.println();

        // 아래 2개의 예제는 만약 canMove에서 movePiece로 진행했으면 프로모션이 발생했을 코드, movePieceTest로 바꾸니 프로모션이 발생하지 않음.
        System.out.println("Test"+ (cnt++) +"---------------");
        board = new Board(true);
        board.setPieceTest(0,7,new King(PieceColor.BLACK));
        board.setPieceTest(1,6,new Pawn(PieceColor.WHITE));
        board.setPieceTest(7,7,new King(PieceColor.WHITE));
        board.setPieceTest(6,1,new Pawn(PieceColor.BLACK));
        System.out.println(board);
        System.out.println("white : "+whiteChecker.canMove(board));
        System.out.println("black : "+blackChecker.canMove(board));
        System.out.println();

        System.out.println("Test"+ (cnt++) +"---------------");
        board = new Board(true);
        board.setPieceTest(0,7,new King(PieceColor.BLACK));
        board.setPieceTest(1,6,new Pawn(PieceColor.BLACK));
        board.setPieceTest(7,7,new King(PieceColor.WHITE));
        board.setPieceTest(6,1,new Pawn(PieceColor.WHITE));
        System.out.println(board);
        System.out.println("white : "+whiteChecker.canMove(board));
        System.out.println("black : "+blackChecker.canMove(board));
        System.out.println();


        System.out.println("Test"+ (cnt++) +"---------------");
        board = new Board(true);
        board.setPieceTest(7,4,new King(PieceColor.WHITE));
        board.setPieceTest(7,1,new Rook(PieceColor.BLACK));
        board.setPieceTest(6,2,new Queen(PieceColor.BLACK));
        board.setPieceTest(0,0,new King(PieceColor.BLACK));
        System.out.println(board);
        System.out.println("white : "+whiteChecker.canMove(board));
        System.out.println("black : "+blackChecker.canMove(board));
        System.out.println();

        System.out.println("Test"+ (cnt++) +"---------------");
        board = new Board();
        board.setPieceTest(3,2,new Pawn(PieceColor.WHITE));
        board.turnChange();

        board.movePiece(1,1,3,1);

        System.out.println(board);
        System.out.println("white : "+whiteChecker.canMove(board));
        System.out.println("black : "+blackChecker.canMove(board));
        System.out.println();
        System.out.println(board);

        System.out.println();
    }

    @Test
    @DisplayName("isOneMoveCheck 테스트")
    public void isOneMoveCheck() {
        board = new Board(true);
        Checker whiteChecker = new Checker(PieceColor.WHITE);
        Checker blackChecker = new Checker(PieceColor.BLACK);
        board.setPieceTest(0,5,new King(PieceColor.WHITE));
        board.setPieceTest(2,6,new Queen(PieceColor.BLACK));
        board.setPieceTest(7,7,new King(PieceColor.BLACK));
        System.out.println(board);
        System.out.println(whiteChecker.isOneMoveCheck(board,board.getCell(0,5),board.getCell(0,6)));
        System.out.println(blackChecker.isOneMoveCheck(board,board.getCell(0,5),board.getCell(0,6)));
        System.out.println(board.movePiece(0,5,0,6));

        System.out.println(board);

    }
}