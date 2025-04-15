package test.java.gameEnd;

import board.Board;
import check.Checker;
import data.PieceColor;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import piece.King;
import piece.Queen;
import piece.Rook;

public class CheckerTest {

    Checker checker;

    @Test
    @DisplayName("King 위치 찾기")
    public void checkBoard() {
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

        Board board2 = new Board(true);

        board2.setPieceTest(5,5,new King(PieceColor.WHITE));
        board2.setPieceTest(4,4,new King(PieceColor.BLACK));

        System.out.println(board2);

        whiteChecker.findKing(board2);
        blackChecker.findKing(board2);
        System.out.println(whiteChecker.King.getRow()+" "+whiteChecker.King.getCol());
        System.out.println(blackChecker.King.getRow() +" "+ blackChecker.King.getCol());

    }




    @Test
    @DisplayName("Check 되는지 확인")
    public void checkBoard2() {
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

        Board board2 = new Board(true);
        board2.setPieceTest(0,7,new King(PieceColor.WHITE));
        board2.setPieceTest(0,0,new Queen(PieceColor.BLACK));
        board2.setPieceTest(7,7,new King(PieceColor.BLACK));
        board2.setPieceTest(7,0,new Queen(PieceColor.WHITE));

        System.out.println(board2);
        whiteChecker.findKing(board2);
        System.out.println(whiteChecker.isCheck(board2));

        blackChecker.findKing(board2);
        System.out.println(blackChecker.isCheck(board2));

        Board board3 = new Board(true);
        board3.setPieceTest(0,7,new King(PieceColor.WHITE));
        board3.setPieceTest(0,6,new Rook(PieceColor.BLACK));
        board3.setPieceTest(7,7,new King(PieceColor.BLACK));


        System.out.println(board3);
        whiteChecker.findKing(board3);
        System.out.println(whiteChecker.isCheck(board3));

        blackChecker.findKing(board2);
        System.out.println(blackChecker.isCheck(board3));
    }

    @Test
    @DisplayName("못움직임")
    public void checkBoard3() {
        Board board = new Board(true);
        Checker whiteChecker = new Checker(PieceColor.WHITE);
        Checker blackChecker = new Checker(PieceColor.BLACK);
        board.setPieceTest(0,6,new King(PieceColor.WHITE));
        board.setPieceTest(2,6,new Queen(PieceColor.BLACK));
        board.setPieceTest(7,7,new King(PieceColor.BLACK));
        System.out.println(board);
        System.out.println(whiteChecker.canMove(board));
        System.out.println(blackChecker.canMove(board));


        Board board2 = new Board(true);
        board2.setPieceTest(0,7,new King(PieceColor.WHITE));
        board2.setPieceTest(2,6,new Queen(PieceColor.BLACK));
        board2.setPieceTest(7,7,new King(PieceColor.BLACK));
        System.out.println(board2);
        System.out.println(whiteChecker.canMove(board2));
        System.out.println(blackChecker.canMove(board2));

        Board board3 = new Board(true);
        board3.setPieceTest(0,7,new King(PieceColor.BLACK));
        board3.setPieceTest(2,6,new Queen(PieceColor.WHITE));
        board3.setPieceTest(7,7,new King(PieceColor.WHITE));
        System.out.println(board3);
        System.out.println(whiteChecker.canMove(board3));
        System.out.println(blackChecker.canMove(board3));

    }
}