package test.java.filemanager;

import board.Board;
import fileManager.FileManager;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerEdgeCaseTests {
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

    @Test // 1. 5개 슬롯 모두 저장된 경우 문제 없이 작동하는지 확인
    void testFullSlotOverwriteBehavior() {
        Board board = new Board();
        for (int i = 1; i <= 5; i++) {
            assertTrue(fileManager.overWriteSavedFile(i, board));
        }

        for (int i = 1; i <= 5; i++) {
            File file = new File(SAVE_DIR + "/savefile" + i + ".txt");
            assertTrue(file.exists());
        }
    }

    @Test // 2. 동일 슬롯에 여러 번 저장 시 counter가 잘 갱신되는지 확인
    void testCounterUpdatesOnOverwrite() {
        Board board = new Board();
        fileManager.overWriteSavedFile(1, board);
        int firstSaveNum = fileManager.getLastSaveFileNum();

        fileManager.overWriteSavedFile(1, board);
        int secondSaveNum = fileManager.getLastSaveFileNum();

        assertEquals(firstSaveNum, secondSaveNum);
    }

    @Test // 3. 저장된 모든 파일 삭제 후 상태 초기화 확인
    void testStateResetAfterAllDeletes() {
        Board board = new Board();
        for (int i = 1; i <= 5; i++) {
            fileManager.overWriteSavedFile(i, board);
        }
        for (int i = 1; i <= 5; i++) {
            fileManager.deleteSavedFile(i);
        }

        assertEquals("NO DATA", fileManager.getLastSavedFile());
        assertEquals(-1, fileManager.getLastSaveFileNum());
    }

    @Test // 4. 저장 이름이 중복되지 않도록 랜덤 생성 검증
    void testUniqueSaveNameGeneration() {
        Board board = new Board();
        Set<String> names = new HashSet<>();

        for (int i = 1; i <= 5; i++) {
            fileManager.overWriteSavedFile(i, board);
            String name = fileManager.getFilename().get(i - 1);
            assertFalse(names.contains(name));
            names.add(name);
        }
    }

    @Test // 5. 저장/불러오기 반복 시 메모리 누수 없이 정상 작동
    void testRepeatedSaveLoadStability() {
        Board board = new Board();
        for (int i = 0; i < 100; i++) {
            int slot = (i % 5) + 1;
            assertTrue(fileManager.overWriteSavedFile(slot, board));
            Board loadTarget = new Board(true);
            assertTrue((fileManager.loadSavedFile(slot, loadTarget)==1));
        }
    }

    @Test // 6. 빈 보드에 null이 아닌 셀 접근 시도
    void testAccessOutsideBoardRange() {
        Board board = new Board(true);
        assertNull(board.getCell(-1, 0));
        assertNull(board.getCell(0, -1));
        assertNull(board.getCell(8, 0));
        assertNull(board.getCell(0, 8));
    }
}