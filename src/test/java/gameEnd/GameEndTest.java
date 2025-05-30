package test.java.gameEnd;

import board.Board;
import check.GameEnd;
import data.PieceColor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import piece.*;

public class GameEndTest {
    GameEnd blackGameEnd = new GameEnd(PieceColor.BLACK);
    GameEnd whiteGameEnd = new GameEnd(PieceColor.WHITE);
    //스테일메이트
    @Test
    @DisplayName("스테일 메이트")
    public void checkStaleMate() {

        Board board = new Board(true, true, true, true);
        board.setPieceTest(0,0,new King(PieceColor.BLACK));
        board.setPieceTest(2,7,new King(PieceColor.WHITE));
        board.setPieceTest(2,1,new Queen(PieceColor.WHITE));
        System.out.println(board);
        System.out.println("black's isStaleMate : " + blackGameEnd.isStaleMate(board));
        System.out.println("white's isStaleMate : " + whiteGameEnd.isStaleMate(board));

        board = new Board(true);
        board.setPieceTest(0,0,new King(PieceColor.BLACK));
        board.setPieceTest(1,7,new Rook(PieceColor.WHITE));
        board.setPieceTest(7,1,new Rook(PieceColor.WHITE));
        board.setPieceTest(7,7,new King(PieceColor.WHITE));

        System.out.println(board);
        System.out.println("black's isStaleMate : " + blackGameEnd.isStaleMate(board));
        System.out.println("white's isStaleMate : " + whiteGameEnd.isStaleMate(board));

        board = new Board(true);
        board.setPieceTest(7,4,new King(PieceColor.WHITE));
        board.setPieceTest(7,1,new Rook(PieceColor.BLACK));
        board.setPieceTest(6,2,new Queen(PieceColor.BLACK));
        board.setPieceTest(0,0,new King(PieceColor.BLACK));
        System.out.println(board);
        System.out.println("black's isStaleMate : " + blackGameEnd.isStaleMate(board));
        System.out.println("white's isStaleMate : " + whiteGameEnd.isStaleMate(board));
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
        System.out.println("isInsufficientPieces : "+blackGameEnd.isInsufficientPieces(board));

        board = new Board();
        System.out.println(board);
        System.out.println("isInsufficientPieces : "+blackGameEnd.isInsufficientPieces(board));

        board = new Board(true);
        board.setPieceTest(3,3,new King(PieceColor.WHITE));
        board.setPieceTest(3,5,new King(PieceColor.BLACK));
        board.setPieceTest(0,0,new Bishop(PieceColor.WHITE));
        board.setPieceTest(1,3,new Bishop(PieceColor.WHITE));
        System.out.println(board);
        System.out.println("isInsufficientPieces : "+blackGameEnd.isInsufficientPieces(board));

        board = new Board(true);
        board.setPieceTest(3,3,new King(PieceColor.WHITE));
        board.setPieceTest(3,5,new King(PieceColor.BLACK));
        board.setPieceTest(0,0,new Knight(PieceColor.WHITE));

        System.out.println(board);
        System.out.println("isInsufficientPieces : "+blackGameEnd.isInsufficientPieces(board));
    }
    //체크메이트 -> 한 열개정도
    @Test
    @DisplayName("체크메이트")
    public void checkCheckMate(){
        Board board = new Board(true);
        board.setPieceTest(0,4,new King(PieceColor.WHITE));
        board.setPieceTest(0,7,new Rook(PieceColor.WHITE));
        board.setPieceTest(0,0,new Rook(PieceColor.WHITE));

        board.setPieceTest(7,4,new King(PieceColor.BLACK));
        board.setPieceTest(7,7,new Rook(PieceColor.BLACK));
        board.setPieceTest(7,0,new Rook(PieceColor.BLACK));

        System.out.println(board);

        System.out.println("black's isCheckMate : " + blackGameEnd.isCheckMate(board));
        System.out.println("white's isCheckMate : " + whiteGameEnd.isCheckMate(board));


        board = new Board();
        System.out.println(board);

        System.out.println("black's isCheckMate : " + blackGameEnd.isCheckMate(board));
        System.out.println("white's isCheckMate : " + whiteGameEnd.isCheckMate(board));

        board = new Board(true);

        // 8번째 행 (검은색 주요 기물)
        board.setPieceTest(0, 0, new Rook(PieceColor.BLACK));
        board.setPieceTest(0, 1, new Knight(PieceColor.BLACK));
        board.setPieceTest(0, 2, new Bishop(PieceColor.BLACK));
        board.setPieceTest(0, 3, new Queen(PieceColor.BLACK));
        board.setPieceTest(0, 4, new King(PieceColor.BLACK));
        board.setPieceTest(0, 5, new Bishop(PieceColor.BLACK));
        board.setPieceTest(0, 6, new Knight(PieceColor.BLACK));
        board.setPieceTest(0, 7, new Rook(PieceColor.BLACK));

// 7번째 행 (검은색 폰)
        for (int col = 0; col < 8; col++) {
            board.setPieceTest(1, col, new Pawn(PieceColor.BLACK));
        }

// 6번째 행 (검은색 나이트와 비숍)
        board.setPieceTest(2, 2, new Knight(PieceColor.BLACK));
        board.setPieceTest(2, 3, null); // 빈 칸
        board.setPieceTest(2, 4, null); // 빈 칸
        board.setPieceTest(2, 5, new Bishop(PieceColor.BLACK));

// 5번째 행 (흰색 퀸과 검은색 폰)
        board.setPieceTest(3, 3, null); // 빈 칸
        board.setPieceTest(3, 4, new Pawn(PieceColor.BLACK));

// 4번째 행 (흰색 비숍과 폰)
        board.setPieceTest(4, 2, null); // 빈 칸
        board.setPieceTest(4, 3, new Bishop(PieceColor.WHITE));
        board.setPieceTest(4, 4, new Pawn(PieceColor.WHITE));

// 3번째 행 (빈 칸)
        for (int col = 0; col < 8; col++) {
            board.setPieceTest(5, col, null);
        }

// 2번째 행 (흰색 폰)
        for (int col = 0; col < 8; col++) {
            board.setPieceTest(6, col,
                    col == 4 ? null : new Pawn(PieceColor.WHITE)); // e2는 빈칸
        }

// 1번째 행 (흰색 주요 기물)
        board.setPieceTest(7, 0, new Rook(PieceColor.WHITE));
        board.setPieceTest(7, 1, new Knight(PieceColor.WHITE));
        board.setPieceTest(7, 2, new Bishop(PieceColor.WHITE));
        board.setPieceTest(7, 3, new Queen(PieceColor.WHITE));
        board.setPieceTest(7, 4, new King(PieceColor.WHITE));
        board.setPieceTest(7, 5, new Bishop(PieceColor.WHITE));
        board.setPieceTest(7, 6, new Knight(PieceColor.WHITE));
        board.setPieceTest(7, 7, new Rook(PieceColor.WHITE));

        System.out.println(board);
        System.out.println("black's isCheckMate : " + blackGameEnd.isCheckMate(board));
        System.out.println("white's isCheckMate : " + whiteGameEnd.isCheckMate(board));


        board = new Board(true);
        board.setPieceTest(7,4,new King(PieceColor.WHITE));
        board.setPieceTest(7,1,new Rook(PieceColor.BLACK));
        board.setPieceTest(6,2,new Queen(PieceColor.BLACK));
        board.setPieceTest(0,0,new King(PieceColor.BLACK));
        System.out.println(board);
        System.out.println("black's isCheckMate : " + blackGameEnd.isCheckMate(board));
        System.out.println("white's isCheckMate : " + whiteGameEnd.isCheckMate(board));

        board = new Board();
        board.movePiece(6 ,5, 5, 5);
        board.turnChange();
        board.movePiece(1, 4 ,3, 4);
        board.turnChange();
        board.movePiece(6, 6 ,4, 6);
        board.turnChange();
        board.movePiece(0, 3 ,4, 7);
        board.turnChange();
        System.out.println(board);
        System.out.println("black's isCheckMate : " + blackGameEnd.isCheckMate(board));
        System.out.println("white's isCheckMate : " + whiteGameEnd.isCheckMate(board));

        board = new Board(true, true, true ,true);
        board.movePiece(6,4,5,4);
        board.turnChange();
        board.movePiece(1,0,3,0);
        board.turnChange();
        board.movePiece(7,3,3,7);
        board.turnChange();
        board.movePiece(0,0,2,0);
        board.turnChange();
        board.movePiece(3,7,3,0);
        board.turnChange();
        board.movePiece(1,7,3,7);
        board.turnChange();
        board.movePiece(6,7,4,7);
        board.turnChange();
        board.movePiece(2,0,2,7);
        board.turnChange();
        board.movePiece(3,0,1,2);
        board.turnChange();
        board.movePiece(1,5,2,5);
        board.turnChange();
        board.movePiece(1,2,1,3);
        board.turnChange();
        System.out.println(board);
        System.out.println("black's isCheckMate : " + blackGameEnd.isCheckMate(board));
        System.out.println("white's isCheckMate : " + whiteGameEnd.isCheckMate(board));

    }
    //되는거 안되는거 다 해야함
    //
}