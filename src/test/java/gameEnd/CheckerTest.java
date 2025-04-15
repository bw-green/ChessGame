package test.java.gameEnd;

import board.Board;
import check.Checker;
import data.PieceColor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import piece.King;
import piece.Queen;

public class CheckerTest {

    Checker checker;

    @Test
    @DisplayName("King 위치 찾기")
    void checkBoard() {
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
    void checkBoard2() {
        Board board = new Board(true);
        board.setPieceTest(0,6,new King(PieceColor.WHITE));
        board.setPieceTest(2,6,new Queen(PieceColor.BLACK));
        board.setPieceTest(3,6,new King(PieceColor.BLACK));
        Checker whiteChecker = new Checker(PieceColor.WHITE);
        Checker blackChecker = new Checker(PieceColor.BLACK);
        System.out.println(board);
        whiteChecker.findKing(board);
        System.out.println(blackChecker.isCheck(board));// 가능


    }

    @Test
    @DisplayName("못움직임")
    void checkBoard3() {
        Board board = new Board(true);
        Checker whiteChecker = new Checker(PieceColor.WHITE);
        Checker blackChecker = new Checker(PieceColor.BLACK);
        System.out.println(board);
        System.out.println(whiteChecker.canMove(board));
        System.out.println(blackChecker.canMove(board));

    }
}
