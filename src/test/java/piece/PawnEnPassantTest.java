package test.java.piece;

import board.Board;
import data.MoveResult;
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
    }

    int[][] helpTest(String start, String end) {
        int[] startCell = Board.notationToCoordinate(start);
        int[] endCell = Board.notationToCoordinate(end);
        return new int[][]{startCell, endCell};
    }
    void templete(String start, String end) {
        int[][] coordinates = helpTest(start, end);
        MoveResult move = board.movePiece(coordinates[0][0], coordinates[0][1], coordinates[1][0], coordinates[1][1]);
        if(move == MoveResult.SUCCESS)
            System.out.println(board);
        else
            System.out.println("테스트 실패");
    }

    @ParameterizedTest
    @CsvSource({
            "b2,b4, a7,a5, b4,b5, g7,g6, b5,a6"
    })
    @DisplayName("백 : 앙파상을 바로 직후 턴에 수행하지 않았을 때 (다른 앙파상 활성 폰 없음)")
    void CanEnPassantTest(String start, String end, String start2, String end2,
                          String start3, String end3, String start4, String end4,String start5, String end5) throws Exception {
        templete(start, end);
        templete(start2, end2);
        templete(start3, end3);
        templete(start4, end4);
        templete(start5, end5);
    }

    @ParameterizedTest
    @CsvSource({
            "b2,b4, a7,a5, b4,b5, g7,g5, b5,a6"
    })
    @DisplayName("백 : 앙파상을 바로 직후 턴에 수행하지 않았을 때 (다른 앙파상 활성 폰 있음)")
    void CanEnPassantTest2(String start, String end, String start2, String end2,
                           String start3, String end3, String start4, String end4,String start5, String end5) throws Exception {
        templete(start, end);
        templete(start2, end2);
        templete(start3, end3);
        templete(start4, end4);
        templete(start5, end5);
    }

    @ParameterizedTest
    @CsvSource({
            "c2,c4, a7,a6, c4,c5, b7,b5, c5,b6"
    })
    @DisplayName("백 : 앙파상을 바로 직후 턴에 수행 했을 때(성공 예측)")
    void CanEnPassantTest3(String start, String end, String start2, String end2,
                           String start3, String end3, String start4, String end4,String start5, String end5) throws Exception {
        templete(start, end);
        templete(start2, end2);
        templete(start3, end3);
        templete(start4, end4);
        templete(start5, end5);
    }

    @ParameterizedTest
    @CsvSource({
            "d2,d4, c7,c5, d4, d5, e7,e5, d5,e6"
    })
    @DisplayName("백 : 양 파일 옆 1칸에 폰이 있고, 둘중 하나만 enpassant이 가능할 때, 가능한 쪽을 캡쳐 시도 (성공예측)")
    void CanEnPassantTest4(String start, String end, String start2, String end2,
                           String start3, String end3, String start4, String end4,String start5, String end5) throws Exception {
        templete(start, end);
        templete(start2, end2);
        templete(start3, end3);
        templete(start4, end4);
        templete(start5, end5);
    }

    @ParameterizedTest
    @CsvSource({
            "d2,d4, c7,c5, d4, d5, e7,e5, d5,c6"
    })
    @DisplayName("백 : 양 파일 옆 1칸에 폰이 있고, 둘중 하나만 enpassant이 가능할 때, 가능하지 않은 쪽을 캡쳐 시도")
    void CanEnPassantTest5(String start, String end, String start2, String end2,
                           String start3, String end3, String start4, String end4,String start5, String end5) throws Exception {
        templete(start, end);
        templete(start2, end2);
        templete(start3, end3);
        templete(start4, end4);
        templete(start5, end5);
    }

    @ParameterizedTest
    @CsvSource({
            "d2,d4, c7,c5, d4,c6"
    })
    @DisplayName("백이 4랭크에서도 앙파상이 가능한 폰을 해당 폰의 뒷랭크로 이동하여 캡쳐할 수 있는가?")
    void CanEnPassantTest6(String start, String end, String start2, String end2,
                           String start3, String end3) throws Exception {
        templete(start, end);
        templete(start2, end2);
        templete(start3, end3);
    }
    @ParameterizedTest
    @CsvSource({
            "d2,d4, g8,f6, d4,d5, f6,h5, d5,d6, e7,e5, d6,e6"
    })
    @DisplayName("백이 6랭크에서도 앙파상이 가능한 폰을 해당 폰의 뒷랭크로 이동하여 캡쳐할 수 있는가?")
    void CanEnPassantTest7(String start, String end, String start2, String end2,
                           String start3, String end3, String start4, String end4,String start5, String end5,
                           String start6, String end6, String start7, String end7) throws Exception {
        templete(start, end);
        templete(start2, end2);
        templete(start3, end3);
        templete(start4, end4);
        templete(start5, end5);
        templete(start6, end6);
        templete(start6, end6);
    }

    @ParameterizedTest
    @CsvSource({
            "d2,d4, a7,a6, d4, d5, f7,f5, d5,e6"
    })
    @DisplayName("백 : 오른쪽 파일 2칸 떨어져있는 앙파상 기물을 캡쳐가 가능한가? (이동자체는 오른쪽대각선 1칸이동")
    void CanEnPassantTest8(String start, String end, String start2, String end2,
                           String start3, String end3, String start4, String end4,String start5, String end5) throws Exception {
        templete(start, end);
        templete(start2, end2);
        templete(start3, end3);
        templete(start4, end4);
        templete(start5, end5);
    }

    @ParameterizedTest
    @CsvSource({
            "d2,d4, a7,a6, d4, d5, f7,f5, d5,f6"
    })
    @DisplayName("백 : 오른쪽 파일 2칸 떨어져있는 앙파상 기물을 캡쳐가 가능한가? (앙파상 기물 뒤로 이동시도)")
    void CanEnPassantTest9(String start, String end, String start2, String end2,
                           String start3, String end3, String start4, String end4,String start5, String end5) throws Exception {
        templete(start, end);
        templete(start2, end2);
        templete(start3, end3);
        templete(start4, end4);
        templete(start5, end5);
    }

    @ParameterizedTest
    @CsvSource({
            "d2,d4, h7,h6, d4, d5, b7,b5, d5,c6"
    })
    @DisplayName("백 : 왼쪽 파일 2칸 떨어져있는 앙파상 기물을 캡쳐가 가능한가? (이동자체는 왼쪽대각선 1칸이동")
    void CanEnPassantTest10(String start, String end, String start2, String end2,
                            String start3, String end3, String start4, String end4,String start5, String end5) throws Exception {
        templete(start, end);
        templete(start2, end2);
        templete(start3, end3);
        templete(start4, end4);
        templete(start5, end5);
    }

    @ParameterizedTest
    @CsvSource({
            "d2,d4, h7,h6, d4, d5, b7,b5, d5,b6"
    })
    @DisplayName("백 : 왼쪽 파일 2칸 떨어져있는 앙파상 기물을 캡쳐가 가능한가? (앙파상 기물 뒤로 이동시도)")
    void CanEnPassantTest11(String start, String end, String start2, String end2,
                            String start3, String end3, String start4, String end4,String start5, String end5) throws Exception {
        templete(start, end);
        templete(start2, end2);
        templete(start3, end3);
        templete(start4, end4);
        templete(start5, end5);
    }
}

