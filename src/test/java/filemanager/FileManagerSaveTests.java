package test.java.filemanager;

import board.Board;
import fileManager.FileManager;
import org.junit.jupiter.api.*;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

class FileManagerSaveTests {
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

    @Test //1. 빈 슬롯에 저장
    void testSaveToEmptySlot() {
        Board board = new Board(true);
        boolean result = fileManager.overWriteSavedFile(1, board);
        assertTrue(result);

        File file = new File(SAVE_DIR + "/savefile1.txt");
        assertTrue(file.exists());
    }

    @Test //2. 기존 슬롯 덮어쓰기
    void testOverwriteExistingSlot() {
        Board board = new Board();
        fileManager.overWriteSavedFile(2, board);
        String originalSaveName = fileManager.getFilename().get(1);

        // 두 번째 저장 (덮어쓰기)
        fileManager.overWriteSavedFile(2, board);
        String newSaveName = fileManager.getFilename().get(1);

        assertNotEquals(originalSaveName, newSaveName);
    }

    @Test //3. 슬롯 번호가 0인 경우
    void testInvalidSlotLow() {
        Board board = new Board();
        boolean result = fileManager.overWriteSavedFile(0, board);
        assertFalse(result);
    }

    @Test //4. 슬롯 번호가 6인 경우
    void testInvalidSlotHigh() {
        Board board = new Board();
        boolean result = fileManager.overWriteSavedFile(6, board);
        assertFalse(result);
    }

    /*슬롯 자리에 int형을 벗어나는 값이나 문자열이 들어오는 경우는 사용자한테 input을 받는 과정에서 걸러질 항목이기에,
    슬롯 번호 오류는 최소한으로 테스트합니다. */

    @Test //5. Board가 null일 때
    void testSaveWithNullBoard() { //Board가 null일 때
        boolean result = fileManager.overWriteSavedFile(1, null);
        assertFalse(result);
    }

    @Test //6. 기물이 없는 Board 저장
    void testSaveBoardWithNullPieces() { //기물이 없는 Board 저장
        Board emptyBoard = new Board(true); // 기물 없는 보드 생성
        boolean result = fileManager.overWriteSavedFile(3, emptyBoard);
        assertTrue(result);

        File file = new File(SAVE_DIR + "/savefile3.txt");
        assertTrue(file.exists());
    }
}