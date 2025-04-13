package test.java.board;

import board.Board;

import data.PieceColor;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import piece.King;
import piece.Queen;

public class BoardTest {
    static Board board;

    @Test
    @DisplayName("보드 움직임 확인용")
    public void testLadderResult() {
        Board board = new Board();

        System.out.println(board.toString());
        System.out.printf("isPathClear : ");
        System.out.println(board.isPathClear(board.getCell(0,0),board.getCell(6,7)));
    }

}
