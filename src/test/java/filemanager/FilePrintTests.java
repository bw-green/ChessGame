package test.java.filemanager;

import board.Board;
import fileManager.FileManager;
import fileManager.FilePrint;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class FilePrintTests {
    private static FileManager fileManager;
    private static FilePrint filePrint;
    private static final String SAVE_DIR = "saves";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeAll
    static void setupClass() {
        fileManager = FileManager.getInstance();
        fileManager.resetTestState();
        filePrint = new FilePrint(fileManager);
    }

    @BeforeEach
    void setup() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void cleanup() {
        System.setOut(originalOut);
        outContent.reset();

        for (int i = 1; i <= 5; i++) {
            File file = new File(SAVE_DIR + "/savefile" + i + ".txt");
            if (file.exists()) file.delete();
        }
        fileManager.resetTestState();
    }

    @Test // 1. saveListPrint 출력 구조 확인
    void testSaveListPrintFormat() {
        filePrint.saveListPrint();
        String output = outContent.toString();

        assertTrue(output.contains("<Last Save File>"));
        assertTrue(output.contains("save 1."));
        assertTrue(output.contains("NO DATA"));
    }

    @Test // 2. saveFilePrint 성공 출력 확인
    void testSaveFilePrintSuccessMessage() {
        Board board = new Board();
        filePrint.saveFilePrint(1, board);

        String output = outContent.toString();
        assertTrue(output.contains("has been created"));
        assertTrue(output.contains("save 1."));
    }

    @Test // 3. saveFilePrint 실패 시 메시지 확인
    void testSaveFilePrintFailureMessage() {
        filePrint.saveFilePrint(0, null); // 유효하지 않은 슬롯 및 null 보드
        String output = outContent.toString();

        assertTrue(output.contains("Failed to save"));
    }

    @Test // 4. deleteFilePrint 성공 메시지 출력 확인
    void testDeleteFilePrintSuccessMessage() {
        Board board = new Board();
        fileManager.overWriteSavedFile(2, board);
        filePrint.deleteFilePrint(2);

        String output = outContent.toString();
        assertTrue(output.contains("has deleted"));
        assertTrue(output.contains("save 2."));
    }

    @Test // 5. deleteFilePrint 실패 시 메시지 출력 확인
    void testDeleteFilePrintFailureMessage() {
        filePrint.deleteFilePrint(3);
        String output = outContent.toString();

        assertTrue(output.contains("Failed to delete"));
    }

    @Test // 6. loadFilePrint 성공 메시지 출력 확인
    void testLoadFilePrintSuccessMessage() {
        Board board = new Board();
        fileManager.overWriteSavedFile(4, board);

        Board newBoard = new Board(true);
        filePrint.loadFilePrint(4, newBoard);

        String output = outContent.toString();
        assertTrue(output.contains("has loaded"));
        assertTrue(output.contains("save 4."));
    }

    @Test // 7. loadFilePrint 실패 메시지 출력 확인
    void testLoadFilePrintFailureMessage() {
        Board board = new Board(true);
        filePrint.loadFilePrint(5, board);
        String output = outContent.toString();

        assertTrue(output.contains("Failed to load"));
    }
}