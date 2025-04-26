package test.java.piece;

import board.Board;
import board.PieceFactory;
import data.MoveResult;
import data.PieceColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import piece.Knight;
import piece.Pawn;
import piece.Piece;

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
        Piece knight = PieceFactory.createPieceFromSymbol("N");
        testBoard.setPieceTest(START_ROW, START_COL, knight);

        // 모든 유효한 L자 이동에 대해 테스트
        for (int i = 0; i < MOVES.length; i++) {
            int dRow = MOVES[i][0];
            int dCol = MOVES[i][1];
            int targetRow = START_ROW + dRow;
            int targetCol = START_COL + dCol;

            MoveResult moved = testBoard.movePiece(START_ROW, START_COL, targetRow, targetCol);
            assertTrue(moved == MoveResult.SUCCESS, "Knight은 " + MOVE_NAMES[i] + " 방향으로 이동 가능해야 한다.");

            // 다음 테스트를 위해, 이동 후에도 Knight를 다시 시작 위치에 배치
            testBoard.setPieceTest(START_ROW, START_COL, knight);
        }
    }

    // 2. 보드 밖 이동 테스트: Knight가 보드 범위를 벗어나면 이동이 거부되어야 함
    @Test
    void testKnightCantMoveOutOfBoard() {
        Piece knight = PieceFactory.createPieceFromSymbol("N");
        // 예를 들어, 좌상단 모서리에 배치
        testBoard.setPieceTest(0, 0, knight);
        // 보드 범위를 벗어난 L자 이동 시도: (-2,-1)
        MoveResult moved = testBoard.movePiece(0, 0, -2, -1);
        assertFalse(moved == MoveResult.SUCCESS, "Knight은 보드 밖으로 이동할 수 없어야 한다.");
    }

    // 3. 적군 캡처 테스트: 목적지에 상대 기물이 있으면 이동(캡처) 가능해야 함
    @Test
    void testKnightCaptureEnemy() {
        Piece knight = PieceFactory.createPieceFromSymbol("N");
        Piece enemyPawn = PieceFactory.createPieceFromSymbol("p");
        testBoard.setPieceTest(START_ROW, START_COL, knight);
        // 첫번째 이동 벡터를 사용하여 테스트
        int targetRow = START_ROW + MOVES[0][0];
        int targetCol = START_COL + MOVES[0][1];
        testBoard.setPieceTest(targetRow, targetCol, enemyPawn);
        MoveResult moved = testBoard.movePiece(START_ROW, START_COL, targetRow, targetCol);
        assertTrue(moved == MoveResult.SUCCESS, "Knight은 적군 기물이 있는 칸으로 이동(캡처)할 수 있어야 한다.");
    }

    // 4. 아군 기물이 있는 경우 이동 불가 테스트: 목적지에 아군 기물이 있으면 이동이 불가능해야 함
    @Test
    void testKnightCannotMoveToAlly() {
        Piece knight = PieceFactory.createPieceFromSymbol("N");
        Piece allyPawn = PieceFactory.createPieceFromSymbol("P");
        testBoard.setPieceTest(START_ROW, START_COL, knight);
        // 두번째 이동 벡터를 사용하여 테스트
        int targetRow = START_ROW + MOVES[1][0];
        int targetCol = START_COL + MOVES[1][1];
        testBoard.setPieceTest(targetRow, targetCol, allyPawn);
        MoveResult moved = testBoard.movePiece(START_ROW, START_COL, targetRow, targetCol);
        assertFalse(moved == MoveResult.SUCCESS, "Knight은 아군 기물이 있는 칸으로 이동할 수 없어야 한다.");
    }

    // 5. 중간 경로 점프 테스트: Knight는 이동 경로에 기물이 있어도 영향을 받지 않아야 한다.
    @Test
    void testKnightJumpingOverPieces() {
        Piece knight = PieceFactory.createPieceFromSymbol("N");
        testBoard.setPieceTest(START_ROW, START_COL, knight);
        // 예: (4,4)에서 (-2,-1) 이동 → (2,3)로 이동 (첫번째 이동 벡터)
        // 중간에 몇 개의 기물을 배치하여 경로가 막혀 있더라도 점프가 가능해야 함.
        testBoard.setPieceTest(3, 4, new Pawn(PieceColor.WHITE));
        testBoard.setPieceTest(3, 3, new Pawn(PieceColor.BLACK));
        int targetRow = START_ROW + MOVES[0][0]; // 4 + (-2) = 2
        int targetCol = START_COL + MOVES[0][1]; // 4 + (-1) = 3
        MoveResult moved = testBoard.movePiece(START_ROW, START_COL, targetRow, targetCol);
        assertTrue(moved == MoveResult.SUCCESS, "Knight은 중간 경로에 기물이 있더라도 뛰어넘어 이동 가능해야 한다.");
    }

    // 추가 테스트: 모서리에서 Knight 이동 가능한지 (예: a1에서)
    @Test
    void testKnightMovementAtCorner() {
        Piece knight = PieceFactory.createPieceFromSymbol("N");
        testBoard.setPieceTest(7, 0, knight); // a1 위치

        // a1에서는 이동 가능한 방향만 두 가지: b3, c2
        int[][] expectedMoves = { {5, 1}, {6, 2} };

        for (int[] move : expectedMoves) {
            MoveResult moved = testBoard.movePiece(7, 0, move[0], move[1]);
            assertTrue(moved == MoveResult.SUCCESS, "Knight은 모서리에서 이동 가능한 방향으로 이동해야 한다.");

            testBoard.setPieceTest(7, 0, knight); // 다시 리셋
        }
    }

    // 추가 테스트: 빈 칸으로 그냥 이동 (캡처 아님)
    @Test
    void testKnightMoveToEmptySquare() {
        Piece knight = PieceFactory.createPieceFromSymbol("N");
        testBoard.setPieceTest(START_ROW, START_COL, knight);

        int targetRow = START_ROW + MOVES[0][0];
        int targetCol = START_COL + MOVES[0][1];

        // 이동하려는 칸은 비어 있음
        MoveResult moved = testBoard.movePiece(START_ROW, START_COL, targetRow, targetCol);
        assertTrue(moved == MoveResult.SUCCESS, "Knight은 빈 칸으로 정상적으로 이동해야 한다.");
    }

    // 추가 테스트: 장애물 종류별(적군/아군) 동시에 있을 때 구분 테스트
    @Test
    void testKnightMovementWithMixedObstacles() {
        Piece knight = PieceFactory.createPieceFromSymbol("N");
        testBoard.setPieceTest(START_ROW, START_COL, knight);

        // 주변에 아군/적군 섞어놓기
        testBoard.setPieceTest(START_ROW + MOVES[0][0], START_COL + MOVES[0][1], PieceFactory.createPieceFromSymbol("P")); // 아군
        testBoard.setPieceTest(START_ROW + MOVES[1][0], START_COL + MOVES[1][1], PieceFactory.createPieceFromSymbol("p")); // 적군

        // 아군 쪽 이동은 실패해야 함
        MoveResult movedToAlly = testBoard.movePiece(START_ROW, START_COL, START_ROW + MOVES[0][0], START_COL + MOVES[0][1]);
        assertFalse(movedToAlly == MoveResult.SUCCESS, "Knight은 아군 기물이 있는 칸으로 이동할 수 없어야 한다.");

        // 적군 쪽 이동은 성공해야 함
        MoveResult movedToEnemy = testBoard.movePiece(START_ROW, START_COL, START_ROW + MOVES[1][0], START_COL + MOVES[1][1]);
        assertTrue(movedToEnemy == MoveResult.SUCCESS, "Knight은 적군 기물이 있는 칸으로 이동(캡처)해야 한다.");
    }
}
