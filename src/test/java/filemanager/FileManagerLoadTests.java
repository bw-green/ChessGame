package test.java.filemanager;

import board.Board;
import fileManager.FileManager;
import org.junit.jupiter.api.*;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerLoadTests {
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

    @Test //1. 정상 파일 불러오기
    void testLoadValidSaveFile() {
        Board originalBoard = new Board();
        fileManager.overWriteSavedFile(1, originalBoard);

        Board loadedBoard = new Board(true);
        boolean result = fileManager.loadSavedFile(1, loadedBoard);

        assertTrue(result);
        assertNotNull(loadedBoard.getCell(0, 0).getPiece()); // Rook 위치 확인
    }

    @Test //2. 존재하지 않는 파일 불러오기
    void testLoadNonExistentFile() {
        Board board = new Board(true);
        boolean result = fileManager.loadSavedFile(2, board); // 파일 생성 없이 불러오기
        assertFalse(result);
    }

    @Test //3. 줄 수 부족한 손상된 파일
    void testCorruptedFileTooFewLines() throws IOException {
        String path = SAVE_DIR + "/savefile3.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write("InvalidSave\n\nWHITE\nrow1 row2\n");
        }
        Board board = new Board(true);
        boolean result = fileManager.loadSavedFile(3, board);
        assertFalse(result);
    }

    @Test //4. 턴 정보가 잘못된 파일
    void testCorruptedFileWrongTurnInfo() throws IOException {
        String path = SAVE_DIR + "/savefile4.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write("BadTurn\n\nNOTATURN\n. . . . . . . .\n. . . . . . . .\n. . . . . . . .\n. . . . . . . .\n. . . . . . . .\n. . . . . . . .\n. . . . . . . .\n. . . . . . . .\n");
        }
        Board board = new Board(true);
        boolean result = fileManager.loadSavedFile(4, board);
        assertFalse(result);
    }

    @Test //5. BLACK 턴 저장 후 불러오기 → 턴 상태 유지 확인
    void testLoadBlackTurnTriggersTurnChange() {
        Board board = new Board();
        board.turnChange(); // 강제로 BLACK으로 만듦
        fileManager.overWriteSavedFile(5, board);

        Board loadedBoard = new Board(true);
        fileManager.loadSavedFile(5, loadedBoard);

        assertEquals(board.getCurrentTurn(), loadedBoard.getCurrentTurn());
    }

    @Test //6. 기물 심볼이 비정상인 경우 처리
    void testLoadDamagedFileWithInvalidSymbols() throws IOException {
        String path = SAVE_DIR + "/savefile2.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write("BadSymbols\n\nWHITE\n@ # $ % ^ & * !\n. . . . . . . .\n. . . . . . . .\n. . . . . . . .\n. . . . . . . .\n. . . . . . . .\n. . . . . . . .\n. . . . . . . .\n");
        }
        Board board = new Board(true);
        boolean result = fileManager.loadSavedFile(2, board);
        assertFalse(result);
    }
}