package test.java.piece;

import board.Board;
import data.PieceColor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import piece.Pawn;

import java.io.ByteArrayInputStream;

public class PawnPromotionTest {
    static Board board;

    @BeforeEach
    void setUpBeforeClass() throws Exception {
        board = new Board();
        board.initializeBoard();

        for(int i=0; i<8; i++){
            board.setPieceTest(0,i,null);
            board.setPieceTest(1,i,new Pawn(PieceColor.WHITE));
        }
        for(int i=0; i<8; i++){
            board.setPieceTest(6,i,new Pawn(PieceColor.BLACK));
            board.setPieceTest(7,i, null);
        }
    }

    int[][] helpTest(String start, String end) {
        int[] startCell = Board.notationToCoordinate(start);
        int[] endCell   = Board.notationToCoordinate(end);
        return new int[][]{ startCell, endCell };
    }

    void promotionTest(String start, String end, String set) {
        //when
        int[][] coordinates =helpTest(start, end);
        System.setIn(new ByteArrayInputStream(set.getBytes()));
        boolean moved=board.movePiece(coordinates[0][0], coordinates[0][1], coordinates[1][0], coordinates[1][1]);


        //then
        System.out.println(moved);
        System.out.println(board);
    }
    // 보드 내 이동 좌표에 문제 없는지 대각만 움직이는지 확인해야함
    // 보드 밖이동
    // 기물이동 사이 아군기물이나 상대기물 존재시
    // 캡처 되는지
    // 아군 기물 있는지
    // 모두 되는지 안되는지 확인해야함
    // 프로모션
    // 앙파상
    //등등
    @ParameterizedTest
    @CsvSource({
            "a7,a8",
            "b7,b8",
            "c7,c8",
            "d7,d8",
            "e7,e8",
            "f7,f8",
            "g7,g8",
            "h7,h8"
    })
    @DisplayName("백의 기물 프로모션 확인용")
    void WhitePawnPromotionTest(String start, String end) {
        promotionTest(start, end,"Q");
    }

    @ParameterizedTest
    @CsvSource({
            "a2,a1",
            "b2,b1",
            "c2,c1",
            "d2,d1",
            "e2,e1",
            "f2,f1",
            "g2,g1",
            "h2,h1"
    })
    @DisplayName("흑의 기물 프로모션 확인용")
    void BlackPawnPromotionTest(String start, String end) {
        promotionTest(start, end,"R");
    }





}
