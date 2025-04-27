package test.java.filemanager;

import board.Board;
import board.PieceFactory;
import fileManager.FileManager;
import org.junit.jupiter.api.*;
import piece.Pawn;
import piece.Piece;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class BoardStatePersistenceTests {
    private static FileManager fileManager;
    private static final String SAVE_DIR = "saves";

    @BeforeAll
    static void setupClass() {
        fileManager = FileManager.getInstance();
        fileManager.resetTestState();
    }

    @AfterEach
    void cleanup() {
        for (int i = 1; i <= 5; i++) {
            File file = new File(SAVE_DIR + "/savefile" + i + ".txt");
            if (file.exists()) file.delete();
        }
    }

    @Test // 1. 초기 보드 상태 저장 후 불러오기 비교
    void testInitialBoardStatePersistence() {
        Board original = new Board();
        fileManager.overWriteSavedFile(1, original);

        Board loaded = new Board(true);
        boolean result = (fileManager.loadSavedFile(1, loaded)==1);

        assertTrue(result);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece originalPiece = original.getPieceAt(row, col);
                Piece loadedPiece = loaded.getPieceAt(row, col);

                if (originalPiece == null || loadedPiece == null) {
                    assertNull(originalPiece);
                    assertNull(loadedPiece);
                } else {
                    assertEquals(originalPiece.getSymbol(), loadedPiece.getSymbol());
                    assertEquals(originalPiece.getColor(), loadedPiece.getColor());
                }
            }
        }
    }

    @Test // 2. 기물 이동 후 저장 -> 불러오기 후 동일한 상태인지 확인
    void testMovedPiecePersistence() {
        Board board = new Board();
        board.movePiece(6, 4, 4, 4); // White pawn e2 -> e4
        fileManager.overWriteSavedFile(2, board);

        Board loaded = new Board(true);
        fileManager.loadSavedFile(2, loaded);

        assertNull(loaded.getPieceAt(6, 4)); // e2가 비어 있어야 함
        assertNotNull(loaded.getPieceAt(4, 4)); // e4에 폰이 있어야 함
        assertEquals("P", loaded.getPieceAt(4, 4).getSymbol());
    }

    @Test // 3. Promotion 상태 저장 -> 불러오기 (강제 배치)
    void testPromotionPersistence() {
        Board board = new Board(true);
        board.setPieceTest(0, 0, PieceFactory.createPieceFromSymbol("Q")); // 퀸 직접 배치
        fileManager.overWriteSavedFile(3, board);

        Board loaded = new Board(true);
        fileManager.loadSavedFile(3, loaded);

        Piece promoted = loaded.getPieceAt(0, 0);
        assertNotNull(promoted);
        assertEquals("Q", promoted.getSymbol());
    }

    @Test // 4. 앙파상 가능 상태 저장 -> 불러오기
    void testEnPassantStatePersistence() {
        Board board = new Board(true);
        board.setPieceTest(6, 4, PieceFactory.createPieceFromSymbol("P")); // White pawn e2
        board.movePiece(6, 4, 4, 4); // e2 -> e4
        fileManager.overWriteSavedFile(4, board);

        Board loaded = new Board(true);
        fileManager.loadSavedFile(4, loaded);

        Piece pawn = loaded.getPieceAt(4, 4);
        Pawn loadedPawn = assertInstanceOf(Pawn.class, pawn);
        assertTrue(loadedPawn.enPassantable);
    }

    @Test // 5. 턴 정보 저장 -> 불러오기 후 동일한 턴인지
    void testTurnInformationPersistence() {
        Board board = new Board();
        board.turnChange(); // BLACK으로 변경
        fileManager.overWriteSavedFile(5, board);

        Board loaded = new Board(true);
        fileManager.loadSavedFile(5, loaded);

        assertEquals(board.getCurrentTurn(), loaded.getCurrentTurn());
    }
}
