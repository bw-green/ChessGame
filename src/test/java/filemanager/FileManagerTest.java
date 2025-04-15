package test.java.filemanager;

import board.Board;
import fileManager.FileManager;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class FileManagerTest {

    private FileManager fileManager;
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
        fileManager = FileManager.getInstance();
    }

    @Test
    void testSingletonBehavior() {
        FileManager anotherInstance = FileManager.getInstance();
        assertSame(fileManager, anotherInstance, "FileManager는 싱글턴이어야 합니다.");
    }

    @Test
    void testSaveToAllSlots() {
        for (int slot = 1; slot <= 5; slot++) {
            assertTrue(fileManager.overWriteSavedFile(slot, board), "슬롯 " + slot + " 저장 실패");
        }
    }

    @Test
    void testLoadFromAllSlots() {
        for (int slot = 1; slot <= 5; slot++) {
            Board loadedBoard = new Board();
            assertTrue(fileManager.loadSavedFile(slot, loadedBoard), "슬롯 " + slot + " 로드 실패");
        }
    }

    @Test
    void testGetSaveNameAndCount() {
        fileManager.overWriteSavedFile(1, board);
        ArrayList<String> filename = fileManager.getFilename();
        String name = filename.get(0);

        assertNotNull(name);
    }

    @Test
    void testInvalidSlotReturnsFalse() {
        assertFalse(fileManager.overWriteSavedFile(0, board));
        assertFalse(fileManager.loadSavedFile(6, board));
    }

    @Test
    void testLastSaveData() {
        fileManager.overWriteSavedFile(2, board);
        String lastSaved = fileManager.getLastSavedFile();
        int lastSlot = fileManager.getLastSaveFileNum();

        assertNotNull(lastSaved);
        assertEquals(1, lastSlot); // 슬롯 인덱스는 내부적으로 0부터 시작하므로
    }

    @Test
    void testBoardLast() {
        //Board originalBoard = new Board();
        fileManager.overWriteSavedFile(2, board);
        String lastSaved = fileManager.getLastSavedFile();
        int lastSlot = fileManager.getLastSaveFileNum();

        assertNotNull(lastSaved);
        assertEquals(1, lastSlot); // 슬롯 인덱스는 내부적으로 0부터 시작하므로
    }
}