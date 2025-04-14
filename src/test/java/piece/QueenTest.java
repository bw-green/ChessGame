package test.java.piece;

import board.Board;
import data.PieceColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import piece.Pawn;
import piece.Queen;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QueenTest {
    private Board testBoard;
    private static final int START_ROW = 4;
    private static final int START_COL = 4;

    // 8방향: 상, 하, 좌, 우, 대각 (우상, 좌상, 우하, 좌하)
    private static final int[][] DIRECTIONS = {
            {-1,  0}, // 상
            {+1,  0}, // 하
            { 0, +1}, // 우
            { 0, -1}, // 좌
            {-1, +1}, // 우상단
            {-1, -1}, // 좌상단
            {+1, +1}, // 우하단
            {+1, -1}  // 좌하단
    };
    private static final String[] DIRECTION_NAMES = {
            "위", "아래", "오른쪽", "왼쪽",
            "우상단", "좌상단", "우하단", "좌하단"
    };

    @BeforeEach
    void setUp() {
        testBoard = new Board(false);  // 초기화 안된 빈 보드
    }

    // 1. Queen 기본 이동 (여러 칸 이동 가능)
    @Test
    void testQueenBasicMovementInAllDirections() {
        Queen queen = new Queen(PieceColor.WHITE);
        testBoard.setPieceTest(START_ROW, START_COL, queen); // 중앙 배치

        // 각 방향으로 3칸 이동하는 경우를 테스트
        for (int i = 0; i < DIRECTIONS.length; i++) {
            int dRow = DIRECTIONS[i][0];
            int dCol = DIRECTIONS[i][1];
            int newRow = START_ROW + 3 * dRow;
            int newCol = START_COL + 3 * dCol;

            boolean moved = testBoard.movePiece(START_ROW, START_COL, newRow, newCol);
            assertTrue(moved, "Queen은 " + DIRECTION_NAMES[i] + " 방향으로 3칸 이동 가능해야 한다.");

            // 다음 이동을 위해 Queen을 다시 중앙에 배치
            testBoard.setPieceTest(START_ROW, START_COL, queen);
        }
    }

    // 2. Queen이 보드 밖으로 이동할 수 없는지 테스트
    @Test
    void testQueenCantMoveOutOfBoard() {
        Queen queen = new Queen(PieceColor.WHITE);
        // 예: 보드의 왼쪽 상단에 배치
        testBoard.setPieceTest(0, 0, queen);
        boolean moved = testBoard.movePiece(0, 0, -1, -1); // 보드 밖으로 이동 시도
        assertFalse(moved, "Queen은 보드 밖으로 이동할 수 없어야 한다.");
    }

    // 3. Queen 캡처 테스트: 대상 칸에 상대 기물이 있는 경우 이동 가능 (캡처)
    @Test
    void testQueenCaptureEnemy() {
        Queen queen = new Queen(PieceColor.WHITE);
        Pawn enemyPawn = new Pawn(PieceColor.BLACK);
        testBoard.setPieceTest(START_ROW, START_COL, queen);
        // 예: 좌우 방향으로 3칸 떨어진 칸에 적 기물을 배치
        int targetRow = START_ROW;
        int targetCol = START_COL + 3;
        testBoard.setPieceTest(targetRow, targetCol, enemyPawn);

        boolean moved = testBoard.movePiece(START_ROW, START_COL, targetRow, targetCol);
        assertTrue(moved, "Queen은 상대 기물을 캡처할 수 있어야 한다.");
    }

    // 4. Queen 경로가 아군 기물에 의해 차단된 경우 이동 실패
    @Test
    void testQueenCannotMoveIfBlockedByAlly() {
        Queen queen = new Queen(PieceColor.WHITE);
        Pawn allyPawn = new Pawn(PieceColor.WHITE);
        testBoard.setPieceTest(START_ROW, START_COL, queen);
        // 예: 좌우 방향으로 Queen이 3칸 이동할 때, 중간 칸 (예를 들어, 2칸 이동한 지점)에 아군 기물이 존재
        // Queen이 (4,4)에서 (4,7)로 이동하려고 할 때, (4,6)에 아군 기물 배치
        testBoard.setPieceTest(4, 6, allyPawn);

        boolean moved = testBoard.movePiece(START_ROW, START_COL, 4, 7);
        assertFalse(moved, "Queen은 경로에 아군 기물이 있으면 이동할 수 없어야 한다.");
    }

    // 5. Queen 경로에 적군 기물이 존재하는 경우, 목적지에만 적군 기물이 있다면 캡처 가능.
    //    하지만, 만약 중간 경로에 기물이 있다면 이동 불가능.
    @Test
    void testQueenCannotJumpOverPieces() {
        Queen queen = new Queen(PieceColor.WHITE);
        Pawn blockingPawn = new Pawn(PieceColor.WHITE); // 중간에 있는 기물이므로 이동 불가
        testBoard.setPieceTest(START_ROW, START_COL, queen);
        // 예: 대각선 우상으로 3칸 이동할 때,
        // 중간 칸: (3,5)에 기물 배치 → (2,6)으로 이동 시도
        testBoard.setPieceTest(START_ROW - 1, START_COL + 1, blockingPawn);
        boolean moved = testBoard.movePiece(START_ROW, START_COL, START_ROW - 2, START_COL + 2);
        assertFalse(moved, "Queen은 중간 경로에 기물이 있으면 이동할 수 없어야 한다.");
    }
}
