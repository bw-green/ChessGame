package test.java.piece;

import board.Board;
import board.PieceFactory;
import data.PieceColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import piece.Pawn;
import piece.Piece;
import piece.Rook;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RookTest {
    private Board testBoard;
    private static final int START_ROW = 4;
    private static final int START_COL = 4;

    // Rook은 수직 및 수평 이동만 가능하므로 4방향: 상, 하, 좌, 우
    private static final int[][] DIRECTIONS = {
            {-1,  0}, // 상
            {+1,  0}, // 하
            { 0, -1}, // 좌
            { 0, +1}  // 우
    };
    private static final String[] DIRECTION_NAMES = {
            "위", "아래", "왼쪽", "오른쪽"
    };

    @BeforeEach
    void setUp() {
        testBoard = new Board(false);  // 초기화 안된 빈 보드
    }

    // 1. Rook 기본 이동 테스트: 각 수직/수평 방향으로 3칸 이동 가능한지 확인
    @Test
    void testRookBasicMovementInAllDirections() {
        Piece rook = PieceFactory.createPieceFromSymbol("R");
        testBoard.setPieceTest(START_ROW, START_COL, rook); // 중앙 배치

        for (int i = 0; i < DIRECTIONS.length; i++) {
            int dRow = DIRECTIONS[i][0];
            int dCol = DIRECTIONS[i][1];
            int newRow = START_ROW + 3 * dRow;
            int newCol = START_COL + 3 * dCol;

            boolean moved = testBoard.movePiece(START_ROW, START_COL, newRow, newCol);
            assertTrue(moved, "Rook은 " + DIRECTION_NAMES[i] + " 방향으로 3칸 이동 가능해야 한다.");

            // 다음 방향 테스트를 위해, 다시 시작 위치에 배치
            testBoard.setPieceTest(START_ROW, START_COL, rook);
        }
    }

    // 2. Rook이 보드 밖으로 이동할 수 없는지 테스트
    @Test
    void testRookCantMoveOutOfBoard() {
        Piece rook = PieceFactory.createPieceFromSymbol("R");
        // 예: 보드의 좌상단 모서리에 배치
        testBoard.setPieceTest(0, 0, rook);
        boolean moved = testBoard.movePiece(0, 0, -1, 0); // 위쪽으로 이동 시도 → 보드 밖
        assertFalse(moved, "Rook은 보드 밖으로 이동할 수 없어야 한다.");
    }

    // 3. Rook 캡처 테스트: 목적지에 상대 기물이 있으면 캡처할 수 있어야 함
    @Test
    void testRookCaptureEnemy() {
        Piece rook = PieceFactory.createPieceFromSymbol("R");
        Piece enemyPawn = PieceFactory.createPieceFromSymbol("p");
        testBoard.setPieceTest(START_ROW, START_COL, rook);
        // 예: 수평 오른쪽 방향으로 3칸 떨어진 곳에 상대 기물 배치
        int targetRow = START_ROW;
        int targetCol = START_COL + 3;
        testBoard.setPieceTest(targetRow, targetCol, enemyPawn);
        boolean moved = testBoard.movePiece(START_ROW, START_COL, targetRow, targetCol);
        assertTrue(moved, "Rook은 상대 기물을 캡처할 수 있어야 한다.");
    }

    // 4. Rook 이동 경로에 아군 기물이 있는 경우 이동 불가 테스트
    @Test
    void testRookCannotMoveIfBlockedByAlly() {
        Piece rook = PieceFactory.createPieceFromSymbol("R");
        Piece allyPawn = PieceFactory.createPieceFromSymbol("P");
        testBoard.setPieceTest(START_ROW, START_COL, rook);
        // 예: 수평 오른쪽 방향으로 이동할 때, 중간 칸 (예: (4,6))에 아군 기물이 존재할 경우
        testBoard.setPieceTest(START_ROW, START_COL + 2, allyPawn);
        boolean moved = testBoard.movePiece(START_ROW, START_COL, START_ROW, START_COL + 3);
        assertFalse(moved, "Rook은 경로에 아군 기물이 있으면 이동할 수 없어야 한다.");
    }

    // 5. Rook은 중간 경로에 기물이 있으면 뛰어넘어 이동할 수 없음 (점프 불가)
    @Test
    void testRookCannotJumpOverPieces() {
        Piece rook = PieceFactory.createPieceFromSymbol("R");
        Piece blockingPawn = PieceFactory.createPieceFromSymbol("p");
        testBoard.setPieceTest(START_ROW, START_COL, rook);
        // 예: 수직 아래 방향으로 이동할 때, 중간 칸 (예: (5,4))에 기물이 있으면, (6,4)로 이동 시도 시 이동 불가
        testBoard.setPieceTest(START_ROW + 1, START_COL, blockingPawn);
        boolean moved = testBoard.movePiece(START_ROW, START_COL, START_ROW + 2, START_COL);
        assertFalse(moved, "Rook은 중간 경로에 기물이 있으면 이동할 수 없어야 한다.");
    }
}
