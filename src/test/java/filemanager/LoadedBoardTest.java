package test.java.filemanager;

import board.Board;
import data.PieceColor;

import fileManager.FileManager;
import org.junit.jupiter.api.Test;
import piece.Knight;
import piece.Pawn;
import piece.Queen;

import static org.junit.jupiter.api.Assertions.*;

public class LoadedBoardTest {

    @Test
    public void testSaveAndLoadBoardConsistency() {
        FileManager manager1 = FileManager.getInstance();
        FileManager manager2 = FileManager.getInstance(); // 싱글턴 확인

        assertSame(manager1, manager2);

        // A: 저장할 보드
        Board boardA = new Board(true); // 비어있는 보드

        // 일부 기물 배치
        boardA.setPieceTest(0, 0, new Queen(PieceColor.BLACK));
        boardA.setPieceTest(1, 1, new Knight(PieceColor.WHITE));
        boardA.setPieceTest(6, 3, new Pawn(PieceColor.WHITE));
        boardA.setPieceTest(2, 6, new Pawn(PieceColor.BLACK));
        // 턴 변경
        boardA.turnChange(); // 이제 턴은 BLACK

        // 저장
        boolean saved = manager1.overWriteSavedFile(3, boardA);
        assertTrue(saved);

        // B: 로드받을 새 보드
        Board boardB = new Board(true);

        // 로드
        boolean loaded = manager2.loadSavedFile(3, boardB);
        assertTrue(loaded);

        // 비교: 각 셀의 기물 타입, 색상 비교
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                var pieceA = boardA.getPieceAt(row, col);
                var pieceB = boardB.getPieceAt(row, col);

                if (pieceA == null || pieceB == null) {
                    assertEquals(pieceA, pieceB, "Mismatch at (" + row + ", " + col + ")");
                } else {
                    assertEquals(pieceA.getClass(), pieceB.getClass(), "Class mismatch at (" + row + ", " + col + ")");
                    assertEquals(pieceA.getColor(), pieceB.getColor(), "Color mismatch at (" + row + ", " + col + ")");
                }
            }
        }

        // 턴 비교
        assertEquals(boardA.getCurrentTurn(), boardB.getCurrentTurn(), "Turn mismatch");
    }
}
