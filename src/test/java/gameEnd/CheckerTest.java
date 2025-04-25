package test.java.gameEnd;

import board.Board;
import check.Checker;
import data.PieceColor;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import piece.King;
import piece.Pawn;
import piece.Queen;
import piece.Rook;

public class CheckerTest {

    Checker checker;

    @Test
    @DisplayName("King 위치 찾기")
    public void findKingTest() {
        Board board = new Board(true);
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

    }




    @Test
    @DisplayName("Check 되는지 확인")
    public void isCheckTest() {
        Board board = new Board(true);
        board.setPieceTest(0,6,new King(PieceColor.WHITE));
        board.setPieceTest(2,6,new Queen(PieceColor.BLACK));
        board.setPieceTest(7,7,new King(PieceColor.BLACK));

        Checker whiteChecker = new Checker(PieceColor.WHITE);
        Checker blackChecker = new Checker(PieceColor.BLACK);
        System.out.println(board);
        whiteChecker.findKing(board);
        System.out.println(whiteChecker.isCheck(board));// 가능

        blackChecker.findKing(board);
        System.out.println(blackChecker.isCheck(board));

        board = new Board(true);
        board.setPieceTest(0,7,new King(PieceColor.WHITE));
        board.setPieceTest(0,0,new Queen(PieceColor.BLACK));
        board.setPieceTest(7,7,new King(PieceColor.BLACK));
        board.setPieceTest(7,0,new Queen(PieceColor.WHITE));

        System.out.println(board);
        whiteChecker.findKing(board);
        System.out.println("white isCheck is "+whiteChecker.isCheck(board));

        blackChecker.findKing(board);
        System.out.println("black isCheck is " + blackChecker.isCheck(board));

        board = new Board(true);
        board.setPieceTest(0,7,new King(PieceColor.WHITE));
        board.setPieceTest(0,6,new Rook(PieceColor.BLACK));
        board.setPieceTest(7,7,new King(PieceColor.BLACK));


        System.out.println(board);
        whiteChecker.findKing(board);
        System.out.println("white isCheck is "+whiteChecker.isCheck(board));

        blackChecker.findKing(board);
        System.out.println("black isCheck is " + blackChecker.isCheck(board));
    }

    @Test
    @DisplayName("못움직임")
    public void canMoveTest() {
        Board board = new Board(true);
        Checker whiteChecker = new Checker(PieceColor.WHITE);
        Checker blackChecker = new Checker(PieceColor.BLACK);
        board.setPieceTest(0,6,new King(PieceColor.WHITE));
        board.setPieceTest(2,6,new Queen(PieceColor.BLACK));
        board.setPieceTest(7,7,new King(PieceColor.BLACK));
        System.out.println(board);
        System.out.println(whiteChecker.canMove(board));
        System.out.println(blackChecker.canMove(board));
        System.out.println();

        board = new Board(true);
        board.setPieceTest(0,7,new King(PieceColor.WHITE));
        board.setPieceTest(2,6,new Queen(PieceColor.BLACK));
        board.setPieceTest(7,7,new King(PieceColor.BLACK));
        System.out.println(board);
        System.out.println(whiteChecker.canMove(board));
        System.out.println(blackChecker.canMove(board));
        System.out.println();

        board = new Board(true);
        board.setPieceTest(0,7,new King(PieceColor.BLACK));
        board.setPieceTest(2,6,new Queen(PieceColor.WHITE));
        board.setPieceTest(7,7,new King(PieceColor.WHITE));
        System.out.println(board);
        System.out.println(whiteChecker.canMove(board));
        System.out.println(blackChecker.canMove(board));
        System.out.println();


        // 아래 2개의 예제는 만약 canMove에서 movePiece로 진행했으면 프로모션이 발생했을 코드, movePieceTest로 바꾸니 프로모션이 발생하지 않음.
        board = new Board(true);
        board.setPieceTest(0,7,new King(PieceColor.BLACK));
        board.setPieceTest(1,6,new Pawn(PieceColor.WHITE));
        board.setPieceTest(7,7,new King(PieceColor.WHITE));
        board.setPieceTest(6,1,new Pawn(PieceColor.BLACK));
        System.out.println(board);
        System.out.println(whiteChecker.canMove(board));
        System.out.println(blackChecker.canMove(board));
        System.out.println();

        board = new Board(true);
        board.setPieceTest(0,7,new King(PieceColor.BLACK));
        board.setPieceTest(1,6,new Pawn(PieceColor.BLACK));
        board.setPieceTest(7,7,new King(PieceColor.WHITE));
        board.setPieceTest(6,1,new Pawn(PieceColor.WHITE));
        System.out.println(board);
        System.out.println(whiteChecker.canMove(board));
        System.out.println(blackChecker.canMove(board));
        System.out.println();

    }
}