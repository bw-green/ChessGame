package test.java;

import board.Board;
import data.MoveResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class CastlingTest {
    static Board board;

    @BeforeEach
    void setUpBeforeClass() throws Exception {
        board = new Board(true, true, true, true);
        board.initializeBoard();
    }

    int[][] helpTest(String start, String end) {
        int[] startCell = Board.notationToCoordinate(start);
        int[] endCell = Board.notationToCoordinate(end);
        return new int[][]{startCell, endCell};
    }
    void templete(String start, String end) {
        int[][] coordinates = helpTest(start, end);
        MoveResult move = board.movePiece(coordinates[0][0], coordinates[0][1], coordinates[1][0], coordinates[1][1]);
        if(move== MoveResult.SUCCESS)
            System.out.println(board);
        else
            System.out.println("테스트 실패");
    }

    @ParameterizedTest
    @CsvSource({
            "g1,f3, a7,a5, e2,e4, b7,b6, f1,a6 , a8, a6, h1, f1, h7, h5, f1, h1, g7, g6, e1 ,g1"
    })
    @DisplayName("킹사이드 : 룩이 한번 움직임")
    void CanCastlingTest1(String start, String end, String start2, String end2,
                          String start3, String end3, String start4, String end4,String start5, String end5,
                          String start6,String end6, String start7, String end7, String start8, String end8,
                          String start9, String end9, String start10, String end10, String start11, String end11) throws Exception {
        templete(start, end);
        templete(start2, end2);
        templete(start3, end3);
        templete(start4, end4);
        templete(start5, end5);
        templete(start6, end6);
        templete(start7, end7);
        templete(start8, end8);
        templete(start9, end9);
        templete(start10, end10);
        templete(start11, end11);
    }
    @ParameterizedTest
    @CsvSource({
            "g1,f3, a7,a5, e2,e4, b7,b6, f1,a6 , a8, a6, e1, f1, h7, h5, f1, e1, g7, g6, e1 ,g1"
    })
    @DisplayName("킹사이드 : 킹이 한번 움직임")
    void CanCastlingTest2(String start, String end, String start2, String end2,
                          String start3, String end3, String start4, String end4,String start5, String end5,
                          String start6,String end6, String start7, String end7, String start8, String end8,
                          String start9, String end9, String start10, String end10, String start11, String end11) throws Exception {
        templete(start, end);
        templete(start2, end2);
        templete(start3, end3);
        templete(start4, end4);
        templete(start5, end5);
        templete(start6, end6);
        templete(start7, end7);
        templete(start8, end8);
        templete(start9, end9);
        templete(start10, end10);
        templete(start11, end11);
    }
    @ParameterizedTest
    @CsvSource({
            "g1,f3, a7,a5, e2,e4, b7,b6, f1,a6 , a8, a6, e1, f1, h7, h5, f1, e1, g7, g6, e1 ,g1"
    })
    @DisplayName("킹사이드 : 킹이 한번(미완성)")
    void CanCastlingTest3(String start, String end, String start2, String end2,
                          String start3, String end3, String start4, String end4,String start5, String end5,
                          String start6,String end6, String start7, String end7, String start8, String end8,
                          String start9, String end9, String start10, String end10, String start11, String end11) throws Exception {
        templete(start, end);
        templete(start2, end2);
        templete(start3, end3);
        templete(start4, end4);
        templete(start5, end5);
        templete(start6, end6);
        templete(start7, end7);
        templete(start8, end8);
        templete(start9, end9);
        templete(start10, end10);
        templete(start11, end11);
    }
}
