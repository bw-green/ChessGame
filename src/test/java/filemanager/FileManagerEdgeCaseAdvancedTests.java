package test.java.filemanager;

import board.Board;
import board.PieceFactory;
import fileManager.FileManager;
import org.junit.jupiter.api.*;
import piece.Piece;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerEdgeCaseAdvancedTests {
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


    // -----------------------------
    // 파일 관련 엣지 케이스
    // -----------------------------

    @Test // 1. 세이브 디렉토리 삭제 후 저장 시도
    void testSaveAfterDeletingDirectory() {
        deleteSaveDirectoryRecursively(); // ✅ 완전 삭제

        Board board = new Board();
        boolean result = fileManager.overWriteSavedFile(1, board);

        assertTrue(result);
        assertTrue(new File(SAVE_DIR + "/savefile1.txt").exists());
    }

    void deleteSaveDirectoryRecursively() {
        File dir = new File(SAVE_DIR);
        if (!dir.exists()) return;

        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) file.delete();
        }
        dir.delete();
    }

    @Test // 2. 빈 파일 불러오기
    void testLoadEmptyFile() throws IOException {
        File file = new File(SAVE_DIR + "/savefile2.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("");
        }

        Board board = new Board(true);
        boolean result = (fileManager.loadSavedFile(2, board)==1);
        assertFalse(result);
    }

    @Test // 3. 파일 이름이 확장자 없이 바뀐 경우
    void testFileWithWrongExtension() throws IOException {
        File file = new File(SAVE_DIR + "/savefile3.tx");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("badformat\n\nWHITE\n. . . . . . . .\n".repeat(8));
        }

        Board board = new Board(true);
        boolean result = (fileManager.loadSavedFile(3, board)==1);
        assertFalse(result); // 실제 파일명은 savefile3.txt가 아님
    }

    @Test // 4. 잘못된 기물 심볼 포함된 파일 (예: @, # 등)
    void testFileWithInvalidPieceSymbols() throws IOException {
        File file = new File(SAVE_DIR + "/savefile4.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("InvalidSymbols\n\nWHITE\n@ # $ % ^ & * !\n. . . . . . . .\n. . . . . . . .\n. . . . . . . .\n. . . . . . . .\n. . . . . . . .\n. . . . . . . .\n. . . . . . . .\n");
        }
        Board board = new Board(true);
        boolean result = (fileManager.loadSavedFile(4, board)==1);
        assertFalse(result);
    }


    // -----------------------------
    // 내부 상태 변수 관련 엣지
    // -----------------------------

    @Test // 5. filename은 존재하나 실제 파일은 없음
    void testFilenameListOutOfSyncWithFileSystem() {
        Board board = new Board();
        fileManager.overWriteSavedFile(4, board);
        new File(SAVE_DIR + "/savefile4.txt").delete();

        Board loadTarget = new Board(true);
        boolean result = (fileManager.loadSavedFile(4, loadTarget)==1);
        assertFalse(result);
    }

    @Test // 6. lastSavedFileNum이 유효 슬롯 범위를 벗어난 경우
    void testInvalidLastSavedFileNumAccess() {
        fileManager.resetTestState();

        for (int i = 1; i <= 5; i++) {
            fileManager.deleteSavedFile(i);
        }
        assertEquals(-1, fileManager.getLastSaveFileNum());
    }


    // -----------------------------
    // 랜덤 저장 이름 관련 엣지
    // -----------------------------

    @Test // 7. 저장 이름이 중복되지 않는지 다시 검증
    void testNoDuplicateSaveNames() {
        Board board = new Board();
        Set<String> names = new HashSet<>();
        for (int i = 1; i <= 5; i++) {
            fileManager.overWriteSavedFile(i, board);
            String name = fileManager.getFilename().get(i - 1);
            assertFalse(names.contains(name));
            names.add(name);
        }
    }


    // -----------------------------
    // 체스 보드 관련 엣지 케이스
    // -----------------------------

    @Test // 8. 킹이 존재하지 않는 보드 저장 및 불러오기
    void testSaveWithoutKing() {
        Board board = new Board(true);
        board.setPieceTest(0, 0, PieceFactory.createPieceFromSymbol("Q"));
        fileManager.overWriteSavedFile(1, board);

        Board loaded = new Board(true);
        boolean result = (fileManager.loadSavedFile(1, loaded)==1);
        assertTrue(result);
    }

    @Test // 9. 앙파상 플래그 설정되어 있으나 실제 기물은 Pawn이 아님
    void testInvalidEnPassantState() {
        Board board = new Board(true);
        Piece fake = PieceFactory.createPieceFromSymbol("Q");
        board.setPieceTest(4, 4, fake);
        fileManager.overWriteSavedFile(2, board);

        Board loaded = new Board(true);
        fileManager.loadSavedFile(2, loaded);

        assertEquals("Q", loaded.getPieceAt(4, 4).getSymbol());
    }

    @Test // 10. 줄 수 부족한 세이브 파일 (8줄 미만)
    void testFileWithTooFewLines() throws IOException {
        File file = new File(SAVE_DIR + "/savefile5.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("TooShort\n\nWHITE\n. . . . . . . .\n. . . . . . . .\n. . . . . . . .\n"); // Only 3 lines
        }
        Board board = new Board(true);
        boolean result = (fileManager.loadSavedFile(5, board)==1);
        assertFalse(result);
    }

    @Test // 11. 기물 개수가 8개 미만인 줄이 포함된 파일
    void testFileWithIncorrectPieceCountInRow() throws IOException {
        File file = new File(SAVE_DIR + "/savefile1.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("BadRowCount\n\nWHITE\n. . . .\n. . . . . . . .\n. . . . . . . .\n. . . . . . . .\n. . . . . . . .\n. . . . . . . .\n. . . . . . . .\n. . . . . . . .\n");
        }
        Board board = new Board(true);
        boolean result = (fileManager.loadSavedFile(1, board)==1);
        assertFalse(result);
    }


    // -----------------------------
    // 테스트 시뮬레이션 전용 엣지
    // -----------------------------

    @Test // 12. 셀 일부만 null인 보드 저장 후 불러오기
    void testBoardWithPartialNullCells() {
        Board board = new Board();
        board.setPieceTest(0, 0, null);
        fileManager.overWriteSavedFile(3, board);

        Board loaded = new Board(true);
        fileManager.loadSavedFile(3, loaded);

        assertNull(loaded.getPieceAt(0, 0));
    }

    @Test // 13. 대소문자 혼합 기물 심볼로 저장된 파일
    void testMixedCasePieceSymbols() throws IOException {
        File file = new File(SAVE_DIR + "/savefile4.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("MixSymb0l\n\nWHITE\nP p Q q R r B b\n. . . . . . . .\n. . . . . . . .\n. . . . . . . .\n. . . . . . . .\n. . . . . . . .\n. . . . . . . .\n. . . . . . . .\n");
        }
        Board board = new Board(true);
        boolean result = (fileManager.loadSavedFile(4, board)==1);
        assertTrue(result);
    }

    @Test // 14. 줄바꿈 형식이 OS마다 다른 경우 처리 확인 (\r\n)
    void testWindowsNewlineFile() throws IOException {
        File file = new File(SAVE_DIR + "/savefile5.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("LineBreak\r\n\r\nWHITE\r\n. . . . . . . .\r\n. . . . . . . .\r\n. . . . . . . .\r\n. . . . . . . .\r\n. . . . . . . .\r\n. . . . . . . .\r\n. . . . . . . .\r\n. . . . . . . .\r\n");
        }
        Board board = new Board(true);
        boolean result = (fileManager.loadSavedFile(5, board)==1);
        assertTrue(result);
    }
}
