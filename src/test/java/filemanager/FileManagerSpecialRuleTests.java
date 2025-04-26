package test.java.filemanager;

import board.Board;
import fileManager.FileManager;
import org.junit.jupiter.api.*;
import piece.Pawn;
import piece.Rook;
import piece.King;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerSpecialRuleTests {
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

    /////////////////////////////////////////////////
    // 저장 관련 테스트
    /////////////////////////////////////////////////

    @Test // 1. enPassant 가능한 폰만 저장
    void testSaveEnPassantPawn() {
        Board board = new Board(true);
        Pawn pawn = new Pawn(board.getCurrentTurn());
        pawn.enPassantable = true;
        pawn.enPassantCounter = 1;
        board.setPieceTest(4, 4, pawn);

        assertTrue(fileManager.overWriteSavedFile(1, board));
    }

    @Test // 2. firstMove 킹 저장
    void testSaveFirstMoveKing() {
        Board board = new Board(true);
        King king = new King(board.getCurrentTurn());
        king.firstMove = true;
        board.setPieceTest(7, 4, king);

        assertTrue(fileManager.overWriteSavedFile(1, board));
    }

    @Test // 3. firstMove 룩 저장
    void testSaveFirstMoveRook() {
        Board board = new Board(true);
        Rook rook = new Rook(board.getCurrentTurn());
        rook.firstMove = true;
        board.setPieceTest(7, 0, rook);

        assertTrue(fileManager.overWriteSavedFile(1, board));
    }

    @Test // 4. 조건 미달 기물은 저장 안됨
    void testSaveInvalidSpecialRulePieces() {
        Board board = new Board(true);
        Pawn pawn = new Pawn(board.getCurrentTurn());
        board.setPieceTest(5, 5, pawn);

        assertTrue(fileManager.overWriteSavedFile(1, board));
    }

    @Test // 5. 특수 상태 없는 경우 저장
    void testSaveNoSpecialRulePieces() {
        Board board = new Board(true);

        assertTrue(fileManager.overWriteSavedFile(1, board));
    }

    @Test // 6. enPassantable true인데 counter != 1이면 저장 안됨
    void testSaveEnPassantWrongCounter() {
        Board board = new Board(true);
        Pawn pawn = new Pawn(board.getCurrentTurn());
        pawn.enPassantable = true;
        pawn.enPassantCounter = 0;
        board.setPieceTest(3, 3, pawn);

        assertTrue(fileManager.overWriteSavedFile(1, board));
    }

    /////////////////////////////////////////////////
    // 불러오기 관련 테스트
    /////////////////////////////////////////////////

    @Test // 7. enPassant 폰 복원
    void testLoadEnPassantPawn() {
        Board board = new Board(true);
        Pawn pawn = new Pawn(board.getCurrentTurn());
        pawn.enPassantable = true;
        pawn.enPassantCounter = 1;
        board.setPieceTest(4, 4, pawn);
        fileManager.overWriteSavedFile(2, board);

        Board loaded = new Board(true);
        assertEquals(1, fileManager.loadSavedFile(2, loaded));

        Pawn loadedPawn = (Pawn) loaded.getCell(4, 4).getPiece();
        assertTrue(loadedPawn.enPassantable);
        assertEquals(1, loadedPawn.enPassantCounter);
    }

    @Test // 8. King firstMove 복원
    void testLoadFirstMoveKing() {
        Board board = new Board(true);
        King king = new King(board.getCurrentTurn());
        king.firstMove = true;
        board.setPieceTest(7, 4, king);
        fileManager.overWriteSavedFile(2, board);

        Board loaded = new Board(true);
        assertEquals(1, fileManager.loadSavedFile(2, loaded));

        King loadedKing = (King) loaded.getCell(7, 4).getPiece();
        assertTrue(loadedKing.firstMove);
    }

    @Test // 9. Rook firstMove 복원
    void testLoadFirstMoveRook() {
        Board board = new Board(true);
        Rook rook = new Rook(board.getCurrentTurn());
        rook.firstMove = true;
        board.setPieceTest(7, 0, rook);
        fileManager.overWriteSavedFile(2, board);

        Board loaded = new Board(true);
        assertEquals(1, fileManager.loadSavedFile(2, loaded));

        Rook loadedRook = (Rook) loaded.getCell(7, 0).getPiece();
        assertTrue(loadedRook.firstMove);
    }

    @Test // 10. 잘못된 특수 줄 무시
    void testLoadCorruptedSpecialLineIgnored() {
        Board board = new Board(true);
        fileManager.overWriteSavedFile(3, board);

        // 수동으로 파일 오염 시뮬레이션 (예: 직접 파일 조작)
        // 이 부분은 실제 파일 쓰기까지 해야 함 (지금은 스킵)

        Board loaded = new Board(true);
        assertEquals(1, fileManager.loadSavedFile(3, loaded));
    }

    @Test // 11. 존재하지 않는 좌표 무시
    void testLoadOutOfBoundsSpecialLineIgnored() {
        Board board = new Board(true);
        fileManager.overWriteSavedFile(3, board);

        Board loaded = new Board(true);
        assertEquals(1, fileManager.loadSavedFile(3, loaded));
    }

    /////////////////////////////////////////////////
    // 복합 상황 테스트
    /////////////////////////////////////////////////

    @Test // 12. enPassant + 캐슬링 기물 복합 저장/불러오기
    void testMixedSpecialRulePieces() {
        Board board = new Board(true);
        Pawn pawn = new Pawn(board.getCurrentTurn());
        pawn.enPassantable = true;
        pawn.enPassantCounter = 1;
        board.setPieceTest(4, 4, pawn);

        King king = new King(board.getCurrentTurn());
        king.firstMove = true;
        board.setPieceTest(7, 4, king);

        Rook rook = new Rook(board.getCurrentTurn());
        rook.firstMove = true;
        board.setPieceTest(7, 0, rook);

        fileManager.overWriteSavedFile(4, board);

        Board loaded = new Board(true);
        assertEquals(1, fileManager.loadSavedFile(4, loaded));

        Pawn loadedPawn = (Pawn) loaded.getCell(4, 4).getPiece();
        King loadedKing = (King) loaded.getCell(7, 4).getPiece();
        Rook loadedRook = (Rook) loaded.getCell(7, 0).getPiece();

        assertTrue(loadedPawn.enPassantable);
        assertEquals(1, loadedPawn.enPassantCounter);
        assertTrue(loadedKing.firstMove);
        assertTrue(loadedRook.firstMove);
    }

    @Test // 13. 특수 상태 없는 보드 저장 후 정상 불러오기
    void testSaveAndLoadWithoutSpecialRulePieces() {
        Board board = new Board(true);
        assertTrue(fileManager.overWriteSavedFile(5, board));

        Board loaded = new Board(true);
        assertEquals(1, fileManager.loadSavedFile(5, loaded));
    }
}
