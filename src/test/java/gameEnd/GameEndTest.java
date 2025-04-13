package test.java.gameEnd;

import board.Board;
import check.GameEnd;
import data.PieceColor;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import piece.Bishop;
import piece.King;
import piece.Queen;

public class GameEndTest {
    GameEnd blackGameEnd = new GameEnd(PieceColor.BLACK);
    GameEnd whiteGameEnd = new GameEnd(PieceColor.WHITE);
    //스테일메이트
    @Test
    @DisplayName("스테일 메이트")
    public void checkStaleMate() {
        Board board = new Board(true);
        board.setPieceTest(0,7,new King(PieceColor.BLACK));
        board.setPieceTest(4,4,new King(PieceColor.WHITE));
        board.setPieceTest(2,6,new Queen(PieceColor.WHITE));
        System.out.println(board);

        if (blackGameEnd.isStaleMate(board)) {
            System.out.println("black's isStaleMate isTrue");
        }
        else{
            System.out.println("black's isStaleMate isFalse");
        }
        if (whiteGameEnd.isStaleMate(board)) {
            System.out.println("white's isStaleMate isTrue");
        }
        else{
            System.out.println("white's isStaleMate isFalse");
        }

    }

    //기물부족
    @Test
    @DisplayName("기물 부족")
    public void checkInsufficientPieces() {
        Board board = new Board(true);

        board.setPieceTest(3,3,new King(PieceColor.WHITE));
        board.setPieceTest(3,5,new King(PieceColor.BLACK));
        board.setPieceTest(0,0,new Bishop(PieceColor.WHITE));
        board.setPieceTest(1,3,new Bishop(PieceColor.BLACK));
        System.out.println(board);
        if (blackGameEnd.isInsufficientPieces(board)) {
            System.out.println("isInsufficientPieces isTrue");
        }
        else{
            System.out.println("isInsufficientPieces isFalse");
        }
    }
    //체크메이트 -> 한 열개정도
    @Test
    @DisplayName("체크메이트")
    public void checkCheckMate(){
        Board board = new Board(true);
        board.setPieceTest(0,7,new King(PieceColor.BLACK));
        board.setPieceTest(2,5,new King(PieceColor.WHITE));
        board.setPieceTest(1,6,new Queen(PieceColor.WHITE));
        System.out.println(board);

        if (blackGameEnd.isCheckMate(board)) {
            System.out.println("black's isCheckMate isTrue");
        }
        else{
            System.out.println("black's isCheckMate isFalse");
        }
        if (whiteGameEnd.isCheckMate(board)) {
            System.out.println("white's isCheckMate isTrue");
        }
        else{
            System.out.println("white's isCheckMate isFalse");
        }
    }
    //되는거 안되는거 다 해야함
    //
}
