package test.java.board;

import board.Board;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class BoardTest {
    static Board board;
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        board = new Board();
        board.initializeBoard();
    }

    @ParameterizedTest
    @CsvSource({
            "e2,e4",
            "a2, a3" ,
            "a1,a2",
            "h2,h4",
            "g4,h4"
    })
    @DisplayName("보드 움직임 확인용")
    void testLadderResult(String start, String end) {
        int[] startKnight = Board.notationToCoordinate(start); // [7,1]
        int[] endKnight   = Board.notationToCoordinate(end); // [5,2]
        board.movePiece(startKnight[0], startKnight[1], endKnight[0], endKnight[1]);
        System.out.println(board.toString());
    }
}
