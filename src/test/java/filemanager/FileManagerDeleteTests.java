package test.java.filemanager;

import board.Board;
import fileManager.FileManager;
import org.junit.jupiter.api.*;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerDeleteTests {
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

    @Test //1. 정상적인 저장 파일 삭제
    void testDeleteExistingFile() {
        Board board = new Board();
        fileManager.overWriteSavedFile(1, board);
        boolean deleted = fileManager.deleteSavedFile(1);

        assertTrue(deleted);
        File file = new File(SAVE_DIR + "/savefile1.txt");
        assertFalse(file.exists());
    }

    @Test //2. 가장 최근 저장 파일 삭제 시 lastSavedFile 갱신
    void testDeleteFileUpdatesLastSavedFile() {
        Board b1 = new Board();
        fileManager.overWriteSavedFile(1, b1);
        fileManager.overWriteSavedFile(2, b1);
        fileManager.overWriteSavedFile(3, b1);

        String lastBefore = fileManager.getLastSavedFile();
        int numBefore = fileManager.getLastSaveFileNum();

        fileManager.deleteSavedFile(numBefore + 1); // 삭제 시도

        String lastAfter = fileManager.getLastSavedFile();
        int numAfter = fileManager.getLastSaveFileNum();

        assertNotEquals(lastBefore, lastAfter);
        assertNotEquals(numBefore, numAfter);
    }

    @Test //3. 유일한 저장 파일 삭제 시 초기화되는지 확인
    void testDeleteOnlyRemainingFileResetsLastSaved() {
        for (int i = 1; i <= 5; i++) {
            fileManager.deleteSavedFile(i);
        }
        fileManager.resetTestState();

        Board board = new Board();
        fileManager.overWriteSavedFile(4, board);
        fileManager.deleteSavedFile(4);

        assertEquals("NO DATA", fileManager.getLastSavedFile());
        assertEquals(-1, fileManager.getLastSaveFileNum());
    }

    @Test //4. 존재하지 않는 슬롯 삭제 시 false 반환
    void testDeleteNonExistentSlot() {
        boolean result = fileManager.deleteSavedFile(5); // 아무것도 저장되지 않음
        assertFalse(result);
    }

    @Test //5. 잘못된 슬롯 번호 (slot = 0)
    void testDeleteInvalidSlotLow() {
        boolean result = fileManager.deleteSavedFile(0);
        assertFalse(result);
    }

    @Test //6. 잘못된 슬롯 번호 (slot = 6)
    void testDeleteInvalidSlotHigh() {
        boolean result = fileManager.deleteSavedFile(6);
        assertFalse(result);
    }
}