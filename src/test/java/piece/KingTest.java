package test.java.piece;

import board.Board;
import board.PieceFactory; // 추가
import data.MoveResult;
import data.PieceColor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import piece.King;
import piece.Pawn;
import piece.Piece;
import piece.Rook;
import test.java.board.BoardTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KingTest {
    private Board testBoard;
    private static final int START_ROW = 4;
    private static final int START_COL = 4;
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
    // 보드 내 이동 좌표에 문제 없는지 대각만 움직이는지 확인해야함
    @Test
    void testKingBasicMovementInAllDirections() {
        testBoard = new Board(false);
        Piece king = PieceFactory.createPieceFromSymbol("K");
        testBoard.setPieceTest(4, 4, king); // e5 중앙


        int currentRow = 4;
        int currentCol = 4;

        for (int i = 0; i < DIRECTIONS.length; i++) {
            int dRow = DIRECTIONS[i][0];
            int dCol = DIRECTIONS[i][1];
            int newRow = currentRow + dRow;
            int newCol = currentCol + dCol;

            MoveResult moved = testBoard.movePiece(currentRow, currentCol, newRow, newCol);
            assertTrue(moved == MoveResult.SUCCESS, "킹은 " + DIRECTION_NAMES[i] + " 방향으로 한 칸 이동 가능해야 한다.");

            // 다음 방향 이동을 위해 킹을 다시 중앙으로 되돌림
            testBoard.setPieceTest(currentRow, currentCol, king);
        }
    }

    // 보드 밖이동
    @Test
    void testKingCantMoveOutOfBoard() {
        testBoard = new Board(false);
        Piece king = PieceFactory.createPieceFromSymbol("K");
        testBoard.setPieceTest(7, 7, king); // 킹을 h8에 배치
        MoveResult move = testBoard.movePiece(7,7,8,8); // false가 의도 결과
        assertTrue(!(move == MoveResult.SUCCESS), "킹은 보드 밖으로 나갈 수 없는데 가능하다고?"); // true가 발생할 시
    }
    // 기물이동 사이 아군기물이나 상대기물 존재시
    // -> 킹은 상관 없지 않나? 캐슬링때만 필요한데 그건 아래 캐슬링 있어서 그냥 생략.

    // 캡처 되는지
    @Test
    void testKingCaptureInAllDirections() {
        testBoard = new Board(false);
        Piece king = PieceFactory.createPieceFromSymbol("K");
        testBoard.setPieceTest(4, 4, king); // 킹을 중앙 e5에 배치

        // 8방향 상대 기물 배치 (BLACK)
        for (int dRow = -1; dRow <= 1; dRow++) {
            for (int dCol = -1; dCol <= 1; dCol++) {
                if (dRow == 0 && dCol == 0) continue; // 자기 자신 제외

                int targetRow = 4 + dRow;
                int targetCol = 4 + dCol;

                // 상대 기물 (예: Pawn) 배치
                testBoard.setPieceTest(targetRow, targetCol, new Pawn(PieceColor.BLACK));

                // 이동 및 캡처 시도
                MoveResult captured = testBoard.movePiece(4, 4, targetRow, targetCol);
                assertTrue(captured == MoveResult.SUCCESS, String.format("킹은 상대 기물을 캡처할 수 있어야 한다: (%d,%d)", targetRow, targetCol));

                // 다음 이동을 위해 킹을 다시 중앙에 배치
                testBoard.setPieceTest(4, 4, king);
            }
        }
    }
    // 아군 기물 있는지
    @Test
    void testKingCannotMoveToAllyInAllDirections() {
        Piece king = PieceFactory.createPieceFromSymbol("K");
        testBoard.setPieceTest(START_ROW, START_COL, king); // 킹 중앙 배치

        for (int i = 0; i < DIRECTIONS.length; i++) {
            int dRow = DIRECTIONS[i][0];
            int dCol = DIRECTIONS[i][1];
            int targetRow = START_ROW + dRow;
            int targetCol = START_COL + dCol;

            // 같은 색 기물 (아군) 배치
            testBoard.setPieceTest(targetRow, targetCol, new Pawn(PieceColor.WHITE));

            // 이동 시도 (성공하면 안 됨)
            MoveResult moved = testBoard.movePiece(START_ROW, START_COL, targetRow, targetCol);
            assertFalse(moved == MoveResult.SUCCESS, "킹은 아군 기물이 있는 " + DIRECTION_NAMES[i] + " 방향으로 이동할 수 없어야 한다.");

            // 다시 중앙에 킹 복귀 (기물이 이동 실패하더라도 상태 보존을 위해 반복)
            testBoard.setPieceTest(START_ROW, START_COL, king);
        }
    }

    // 케슬링
    @Test
    void testKingSideCastlingSuccess() {
        testBoard = new Board(false);

        Piece king = PieceFactory.createPieceFromSymbol("K");
        Piece rook = PieceFactory.createPieceFromSymbol("R");
        // 킹과 룩 배치 (e1, h1)
        testBoard.setPieceTest(7, 4, king);
        testBoard.setPieceTest(7, 7, rook);

        // 캐슬링을 수행 (예: e1 → g1)
        MoveResult castled = testBoard.movePiece(7, 4, 7, 6); // g1

        assertTrue(castled == MoveResult.SUCCESS, "킹사이드 캐슬링이 정상적으로 수행되어야 한다.");

        // 킹이 g1에 있는지, 룩이 f1에 있는지 확인
        assertTrue(testBoard.getPieceAt(7, 6) instanceof King, "킹은 g1에 있어야 한다.");
        assertTrue(testBoard.getPieceAt(7, 5) instanceof Rook, "룩은 f1에 있어야 한다.");
    }

    // 가려고 하는데가 체크일시
    @Test
    void testKingCannotMoveIntoCheck() { // 킹이 체크가 되는 칸으로 이동하려는 경우!
        testBoard = new Board(false);
        Piece king = PieceFactory.createPieceFromSymbol("K");
        Piece enemyRook = PieceFactory.createPieceFromSymbol("r");

        // 킹을 중앙 e5 (4,4)에 배치
        testBoard.setPieceTest(4, 4, king);
        // 적 룩을 f5 (4,5)에 배치 → e6 (3,4)는 룩의 공격 방향
        testBoard.setPieceTest(4, 6, enemyRook);

        // 킹이 오른쪽(f5)으로 이동 시도 → 룩 공격 방향
        MoveResult movedIntoCheck = testBoard.movePiece(4, 4, 4, 5); // e5 → f5
        assertTrue(!(movedIntoCheck == MoveResult.SUCCESS), "킹은 체크되는 위치로 이동할 수 없어야 한다.");
    }
    @Test
    void testKingCanEscapeFromCheck() { // 킹이 체크에서 벗어나려고 이동하는 경우(탈출)
        testBoard = new Board(false);
        Piece king = PieceFactory.createPieceFromSymbol("K");
        Piece enemyRook = PieceFactory.createPieceFromSymbol("r");

        // 적 룩이 같은 행에서 킹을 공격
        testBoard.setPieceTest(4, 0, enemyRook); // a5
        testBoard.setPieceTest(4, 4, king);      // e5

        // 가정: 이동 전 상태에서 Board는 킹이 체크당하고 있음을 판단할 수 있어야 함

        // 킹이 한 칸 위로 피함 (e4)
        MoveResult movedOutOfCheck = testBoard.movePiece(4, 4, 3, 4); // e5 → e4
        assertTrue(movedOutOfCheck == MoveResult.SUCCESS, "킹은 체크 상태에서 벗어나는 이동은 가능해야 한다.");
    }

    // 추가 테스트: 퀸 사이드 캐슬링
    @Test
    void testQueenSideCastlingSuccess() {
        testBoard = new Board(false);
        Piece king = PieceFactory.createPieceFromSymbol("K");
        Piece rook = PieceFactory.createPieceFromSymbol("R");

        testBoard.setPieceTest(7, 4, king); // e1
        testBoard.setPieceTest(7, 0, rook); // a1

        MoveResult castled = testBoard.movePiece(7, 4, 7, 2); // e1 → c1

        assertTrue(castled == MoveResult.SUCCESS, "퀸사이드 캐슬링이 정상적으로 수행되어야 한다.");
        assertTrue(testBoard.getPieceAt(7, 2) instanceof King, "킹은 c1에 있어야 한다.");
        assertTrue(testBoard.getPieceAt(7, 3) instanceof Rook, "룩은 d1에 있어야 한다.");
    }

    // 추가 테스트: 중간에 기물이 있을 때 캐슬링 시도
    @Test
    void testCastlingFailsIfBlocked() {
        testBoard = new Board(false);
        Piece king = PieceFactory.createPieceFromSymbol("K");
        Piece rook = PieceFactory.createPieceFromSymbol("R");
        Piece pawn = PieceFactory.createPieceFromSymbol("P"); // 중간 장애물

        testBoard.setPieceTest(7, 4, king); // e1
        testBoard.setPieceTest(7, 7, rook); // h1
        testBoard.setPieceTest(7, 5, pawn); // f1

        MoveResult castled = testBoard.movePiece(7, 4, 7, 6); // e1 → g1
        assertFalse(castled == MoveResult.SUCCESS, "중간에 기물이 있으면 캐슬링이 실패해야 한다.");
    }
}
