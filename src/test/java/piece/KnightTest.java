package test.java.piece;

import board.Board;
import data.PieceColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import piece.Knight;
import piece.Pawn;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KnightTest {
    private Board testBoard;
    private static final int START_ROW = 4;
    private static final int START_COL = 4;

    // Knight가 이동 가능한 8가지 L자 이동 벡터
    // (행, 열) 변화: 2칸, 1칸 이동
    private static final int[][] MOVES = {
            {-2, -1},
            {-2, +1},
            {-1, -2},
            {-1, +2},
            {+1, -2},
            {+1, +2},
            {+2, -1},
            {+2, +1}
    };
    // 테스트 설명용 이름 (원하는 형태로 자유롭게 수정)
    private static final String[] MOVE_NAMES = {
            "좌상단1", "우상단1", "좌상단2", "우상단2",
            "좌하단1", "우하단1", "좌하단2", "우하단2"
    };

    @BeforeEach
    void setUp() {
        testBoard = new Board(false);
    }

    // 1. 기본 이동 테스트: Knight가 모든 8가지 L자 이동을 수행할 수 있는지
    @Test
    void testKnightBasicMovement() {
        Knight knight = new Knight(PieceColor.WHITE);
        testBoard.setPieceTest(START_ROW, START_COL, knight);

        // 모든 유효한 L자 이동에 대해 테스트
        for (int i = 0; i < MOVES.length; i++) {
            int dRow = MOVES[i][0];
            int dCol = MOVES[i][1];
            int targetRow = START_ROW + dRow;
            int targetCol = START_COL + dCol;

            boolean moved = testBoard.movePiece(START_ROW, START_COL, targetRow, targetCol);
            assertTrue(moved, "Knight은 " + MOVE_NAMES[i] + " 방향으로 이동 가능해야 한다.");

            // 다음 테스트를 위해, 이동 후에도 Knight를 다시 시작 위치에 배치
            testBoard.setPieceTest(START_ROW, START_COL, knight);
        }
    }

    // 2. 보드 밖 이동 테스트: Knight가 보드 범위를 벗어나면 이동이 거부되어야 함
    @Test
    void testKnightCantMoveOutOfBoard() {
        Knight knight = new Knight(PieceColor.WHITE);
        // 예를 들어, 좌상단 모서리에 배치
        testBoard.setPieceTest(0, 0, knight);
        // 보드 범위를 벗어난 L자 이동 시도: (-2,-1)
        boolean moved = testBoard.movePiece(0, 0, -2, -1);
        assertFalse(moved, "Knight은 보드 밖으로 이동할 수 없어야 한다.");
    }

    // 3. 적군 캡처 테스트: 목적지에 상대 기물이 있으면 이동(캡처) 가능해야 함
    @Test
    void testKnightCaptureEnemy() {
        Knight knight = new Knight(PieceColor.WHITE);
        Pawn enemyPawn = new Pawn(PieceColor.BLACK);
        testBoard.setPieceTest(START_ROW, START_COL, knight);
        // 첫번째 이동 벡터를 사용하여 테스트
        int targetRow = START_ROW + MOVES[0][0];
        int targetCol = START_COL + MOVES[0][1];
        testBoard.setPieceTest(targetRow, targetCol, enemyPawn);
        boolean moved = testBoard.movePiece(START_ROW, START_COL, targetRow, targetCol);
        assertTrue(moved, "Knight은 적군 기물이 있는 칸으로 이동(캡처)할 수 있어야 한다.");
    }

    // 4. 아군 기물이 있는 경우 이동 불가 테스트: 목적지에 아군 기물이 있으면 이동이 불가능해야 함
    @Test
    void testKnightCannotMoveToAlly() {
        Knight knight = new Knight(PieceColor.WHITE);
        Pawn allyPawn = new Pawn(PieceColor.WHITE);
        testBoard.setPieceTest(START_ROW, START_COL, knight);
        // 두번째 이동 벡터를 사용하여 테스트
        int targetRow = START_ROW + MOVES[1][0];
        int targetCol = START_COL + MOVES[1][1];
        testBoard.setPieceTest(targetRow, targetCol, allyPawn);
        boolean moved = testBoard.movePiece(START_ROW, START_COL, targetRow, targetCol);
        assertFalse(moved, "Knight은 아군 기물이 있는 칸으로 이동할 수 없어야 한다.");
    }

    // 5. 중간 경로 점프 테스트: Knight는 이동 경로에 기물이 있어도 영향을 받지 않아야 한다.
    @Test
    void testKnightJumpingOverPieces() {
        Knight knight = new Knight(PieceColor.WHITE);
        testBoard.setPieceTest(START_ROW, START_COL, knight);
        // 예: (4,4)에서 (-2,-1) 이동 → (2,3)로 이동 (첫번째 이동 벡터)
        // 중간에 몇 개의 기물을 배치하여 경로가 막혀 있더라도 점프가 가능해야 함.
        testBoard.setPieceTest(3, 4, new Pawn(PieceColor.WHITE));
        testBoard.setPieceTest(3, 3, new Pawn(PieceColor.BLACK));
        int targetRow = START_ROW + MOVES[0][0]; // 4 + (-2) = 2
        int targetCol = START_COL + MOVES[0][1]; // 4 + (-1) = 3
        boolean moved = testBoard.movePiece(START_ROW, START_COL, targetRow, targetCol);
        assertTrue(moved, "Knight은 중간 경로에 기물이 있더라도 뛰어넘어 이동 가능해야 한다.");
    }
}
