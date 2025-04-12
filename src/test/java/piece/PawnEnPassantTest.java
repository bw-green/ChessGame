package test.java.piece;

import board.Board;
import data.PieceColor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import piece.Pawn;

public class PawnEnPassantTest {
    static Board board;

    @BeforeEach
    void setUpBeforeClass() throws Exception {
        board = new Board();
        board.initializeBoard();

        board.setPieceTest(3, 1, new Pawn(PieceColor.WHITE));
    }

    int[][] helpTest(String start, String end) {
        int[] startCell = Board.notationToCoordinate(start);
        int[] endCell   = Board.notationToCoordinate(end);
        return new int[][]{ startCell, endCell };
    }

    @ParameterizedTest
    @CsvSource({
            "a7,a5, b5,a6",
    })
    @DisplayName("백의 기물 프로모션 확인용")
    void CanEnPassantTest(String start, String end, String start2, String end2) throws Exception {
        System.out.println(board);
        int[][] coordinates =helpTest(start, end);
        boolean moved=board.movePiece(coordinates[0][0], coordinates[0][1], coordinates[1][0], coordinates[1][1]);
        System.out.println(board);
        int[][] coordinates2 =helpTest(start2, end2);
        boolean moved2=board.movePiece(coordinates2[0][0], coordinates2[0][1], coordinates2[1][0], coordinates2[1][1]);
        System.out.println(board);
    }


    @ParameterizedTest
    @CsvSource({
            "a7,a5, a2,a4",
            "c7,c5, b5 ,a6"
    })
    @DisplayName("백의 기물 프로모션 확인용")
    void CantEnPassantTest(String start, String end, String start2, String end2) throws Exception {
        System.out.println(board);
        int[][] coordinates =helpTest(start, end);
        boolean moved=board.movePiece(coordinates[0][0], coordinates[0][1], coordinates[1][0], coordinates[1][1]);
        System.out.println(board);
        int[][] coordinates2 =helpTest(start2, end2);
        boolean moved2=board.movePiece(coordinates2[0][0], coordinates2[0][1], coordinates2[1][0], coordinates2[1][1]);
        System.out.println(board);
    }
}


