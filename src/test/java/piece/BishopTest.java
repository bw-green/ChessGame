package test.java.piece;

import board.Board;
import data.PieceColor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import piece.Bishop;
import piece.Piece;


public class BishopTest {
    Board board;
    @BeforeEach
    void setUpBeforeClass() throws Exception {
        board = new Board(true);
    }

    int[][] helpTest(String start, String end) {
        int[] startCell = Board.notationToCoordinate(start);
        int[] endCell   = Board.notationToCoordinate(end);
        return new int[][]{ startCell, endCell };
    }

    @ParameterizedTest
    @CsvSource({
            "b1,c2",
            "b1, e4" ,
            "b1,f5",
            "b1,d3"
    })
    @DisplayName("보드 우 상향이동 확인용")
    void testBishopRightUpMove(String start, String end) {

        //given
        Piece bishop = new Bishop(PieceColor.BLACK);
        board.setPieceTest(7,1,bishop);


        //when
        int[][] coordinates =helpTest(start, end);
        boolean moved=board.movePiece(coordinates[0][0], coordinates[0][1], coordinates[1][0], coordinates[1][1]);


        //then
        System.out.println(moved);
        System.out.println(board);
    }

    @ParameterizedTest
    @CsvSource({
            "d2,e3",
            "d2, f4" ,
            "d2,g5",
            "d2,h6"
    })
    @DisplayName("보드 우 하향이동 확인용")
    void testBishopLeftDownMove(String start, String end) {
        //given
        Piece bishop = new Bishop(PieceColor.WHITE);
        board.setPieceTest(6,3,bishop);


        //when
        int[][] coordinates =helpTest(start, end);
        boolean moved=board.movePiece(coordinates[0][0], coordinates[0][1], coordinates[1][0], coordinates[1][1]);


        //then

        Assertions.assertTrue(moved);
        System.out.println(board);
    }



}
