package test.java.piece;

import board.Board;
import org.junit.jupiter.api.BeforeAll;

public class KingTest {
    static Board board;
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        board = new Board();
        board.initializeBoard();
    }
    // 보드 내 이동 좌표에 문제 없는지 대각만 움직이는지 확인해야함
    // 보드 밖이동
    // 기물이동 사이 아군기물이나 상대기물 존재시
    // 캡처 되는지
    // 아군 기물 있는지
    // 모두 되는지 안되는지 확인해야함
    // 케슬링
    // 체크일시, 체크가려고 하는데가 체크일시
    //등등
}
