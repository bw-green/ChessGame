package test.java.gameEnd;

import board.Board;
import check.Checker;
import data.PieceColor;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import piece.King;
import piece.Queen;

public class CheckerTest {

    Checker checker;

    @Test
    @DisplayName("King 위치 찾기")
    public void checkBoard() {
        Board board = new Board(true);
        board.setPieceTest(3,3,new King(PieceColor.WHITE));
        board.setPieceTest(3,4,new King(PieceColor.BLACK));

        Checker whiteChecker = new Checker(PieceColor.WHITE);
        Checker blackChecker = new Checker(PieceColor.BLACK);
        System.out.println(board);
        whiteChecker.findKing(board);
        blackChecker.findKing(board);
        System.out.println(whiteChecker.King.getCol());
        System.out.println(blackChecker.King.getCol());
    }


    @Test
    @DisplayName("Check 되는지 확인")
    public void checkBoard2() {
        Board board = new Board(true);
        board.setPieceTest(0,6,new King(PieceColor.WHITE));
        board.setPieceTest(2,6,new Queen(PieceColor.BLACK));

        Checker whiteChecker = new Checker(PieceColor.WHITE);
        Checker blackChecker = new Checker(PieceColor.BLACK);
        System.out.println(board);
        whiteChecker.findKing(board);
        System.out.println(whiteChecker.isCheck(board));// 가능

        blackChecker.findKing(board);
        System.out.println(blackChecker.isCheck(board));


    }

    @Test
    @DisplayName("못움직임")
    public void checkBoard3() {
        Board board = new Board(true);
        Checker whiteChecker = new Checker(PieceColor.WHITE);
        Checker blackChecker = new Checker(PieceColor.BLACK);
        board.setPieceTest(0,6,new King(PieceColor.WHITE));
        board.setPieceTest(2,6,new Queen(PieceColor.BLACK));

        System.out.println(board);
        System.out.println(whiteChecker.canMove(board));
        System.out.println(blackChecker.canMove(board));

    }
}