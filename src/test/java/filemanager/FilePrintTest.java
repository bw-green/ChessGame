package test.java.filemanager;

import board.Board;
import fileManager.FileManager;
import fileManager.FilePrint;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FilePrintTest {

    private FilePrint filePrint;
    private FileManager fileManager;
    private Board board;

    @BeforeEach
    void setUp() {
        fileManager = FileManager.getInstance();
        board = new Board();
        filePrint = new FilePrint(fileManager);
    }

    @Test
    void testSaveThroughFilePrint() {
        filePrint.saveFilePrint(3, board);
    }

    @Test
    void testLoadThroughFilePrint() {
        filePrint.saveFilePrint(4, board);
        Board loaded = new Board();
        filePrint.loadFilePrint(4, loaded);
    }

    @Test
    void testFilePrintAndManagerSync() {
        filePrint.saveFilePrint(5, board);
        ArrayList<String> filename = fileManager.getFilename();
        String fromPrint = filename.get(4);
        assertNotNull(fromPrint);
    }

    @Test
    void testFilePrintDELETE() {
        filePrint.deleteFilePrint(5);
        ArrayList<String> filename = fileManager.getFilename();
        String fromPrint = filename.get(4);
        assertNotNull(fromPrint);
    }

    @Test
    void testFilePrintWithNewInstanceStillWorks() {
        FileManager fileManager1 = FileManager.getInstance();
        FilePrint anotherPrint = new FilePrint(fileManager1);
        anotherPrint.saveFilePrint(2, board);
        anotherPrint.loadFilePrint(2, new Board());
    }

    @Test
    void testSaveFilListPrint() {
        filePrint.saveListPrint();
    }
}
