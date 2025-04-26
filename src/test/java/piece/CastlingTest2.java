package test.java.piece;

import board.Board;
import data.PieceColor;
import data.MoveResult;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import piece.King;
import piece.Rook;

public class CastlingTest2 {
    static Board board;

    @BeforeEach
    void setUpBeforeClass() throws Exception {
        board = new Board();
        board.initializeBoard();
    }

    void setWhiteCastlingable() throws Exception {
        board = new Board();
        board.initializeBoard();
        board.setPieceTest(7,5, null);
        board.setPieceTest(7,6,null);
        board.setPieceTest(7,3,null);
        board.setPieceTest(7,2,null);
        board.setPieceTest(7,1,null);
    }

    void setBlackCastlingable() throws Exception {
        board = new Board();
        board.initializeBoard();
        board.setPieceTest(0,5, null);
        board.setPieceTest(0,6,null);
        board.setPieceTest(0,3,null);
        board.setPieceTest(0,2,null);
        board.setPieceTest(0,1,null);
    }

    int[][] helpTest(String start, String end) {
        int[] startCell = Board.notationToCoordinate(start);
        int[] endCell = Board.notationToCoordinate(end);
        return new int[][]{startCell, endCell};
    }
    void templete(String start, String end) {
        int[][] coordinates = helpTest(start, end);
        MoveResult move = board.movePiece(coordinates[0][0], coordinates[0][1], coordinates[1][0], coordinates[1][1]);
        if (move == MoveResult.SUCCESS)
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
            "b1,c3, a7,a5, d2,d4, b7,b6, c1,f4 , a8, a6, a1, c1, h7, h5, c1, a1, g7, g6, d1, d3, g8, f6, e1 ,c1"
    })
    @DisplayName("퀸사이드 : 룩이 한번 움직임")
    void CanCastlingTest3(String start, String end, String start2, String end2,
                          String start3, String end3, String start4, String end4,String start5, String end5,
                          String start6,String end6, String start7, String end7, String start8, String end8,
                          String start9, String end9, String start10, String end10, String start11, String end11,
                          String start12,String end12,String start13, String end13) throws Exception {
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
        templete(start12, end12);
        templete(start13, end13);
    }

    @ParameterizedTest
    @CsvSource({
            "b1,c3, a7,a5, d2,d4, b7,b6, c1,f4 , a8, a6, d1, d3, h7, h5, e1, d1, g7, g6, d1, e1, g8, f6, e1 ,c1"
    })
    @DisplayName("퀸사이드 : 킹이 한번 움직임")
    void CanCastlingTest4(String start, String end, String start2, String end2,
                          String start3, String end3, String start4, String end4,String start5, String end5,
                          String start6,String end6, String start7, String end7, String start8, String end8,
                          String start9, String end9, String start10, String end10, String start11, String end11,
                          String start12,String end12,String start13, String end13) throws Exception {
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
        templete(start12, end12);
        templete(start13, end13);
    }


    @ParameterizedTest
    @CsvSource({
            "e1,g1"
    })
    @DisplayName("킹사이드 : 체크일 때 캐슬링 테스트")
    void CanCastlingTest10(String start,String end) throws Exception {
        setWhiteCastlingable();
        board.setPieceTest(4,5,new Rook(PieceColor.BLACK));
        board.setPieceTest(6,5,null);
        System.out.println(board);
        templete(start,end);
    }

    @ParameterizedTest
    @CsvSource({
            "e1,g1"
    })
    @DisplayName("킹사이드 : 체크일 때 캐슬링 테스트2")
    void CanCastlingTest11(String start,String end) throws Exception {
        setWhiteCastlingable();
        board.setPieceTest(4,6,new Rook(PieceColor.BLACK));
        board.setPieceTest(6,6,null);
        System.out.println(board);
        templete(start,end);
    }

    @ParameterizedTest
    @CsvSource({
            "e1,g1"
    })
    @DisplayName("킹사이드 : 체크일 때 캐슬링 테스트3(성공예측)")
    void CanCastlingTest12(String start,String end) throws Exception {
        setWhiteCastlingable();
        board.setPieceTest(4,7,new Rook(PieceColor.BLACK));
        board.setPieceTest(6,7,null);
        System.out.println(board);
        templete(start,end);
    }

    @ParameterizedTest
    @CsvSource({
            "e1,c1"
    })
    @DisplayName("퀸사이드 : 체크일 때 캐슬링 테스트")
    void CanCastlingTest13(String start,String end) throws Exception {
        setWhiteCastlingable();
        board.setPieceTest(4,3,new Rook(PieceColor.BLACK));
        board.setPieceTest(6,3,null);
        System.out.println(board);
        templete(start,end);
    }

    @ParameterizedTest
    @CsvSource({
            "e1,c1"
    })
    @DisplayName("퀸사이드 : 체크일 때 캐슬링 테스트2")
    void CanCastlingTest14(String start,String end) throws Exception {
        setWhiteCastlingable();
        board.setPieceTest(4,2,new Rook(PieceColor.BLACK));
        board.setPieceTest(6,2,null);
        System.out.println(board);
        templete(start,end);
    }

    @ParameterizedTest
    @CsvSource({
            "e1,c1"
    })
    @DisplayName("퀸사이드 : 체크일 때 캐슬링 테스트3 (성공예측)")
    void CanCastlingTest15(String start,String end) throws Exception {
        setWhiteCastlingable();
        board.setPieceTest(4,1,new Rook(PieceColor.BLACK));
        board.setPieceTest(6,1,null);
        System.out.println(board);
        templete(start,end);
    }

    @ParameterizedTest
    @CsvSource({
            "e1,c1"
    })
    @DisplayName("퀸사이드 : 체크일 때 캐슬링 테스트3 (성공예측)")
    void CanCastlingTest16(String start,String end) throws Exception {
        setWhiteCastlingable();
        board.setPieceTest(4,0,new Rook(PieceColor.BLACK));
        board.setPieceTest(6,0,null);
        System.out.println(board);
        templete(start,end);
    }
}


