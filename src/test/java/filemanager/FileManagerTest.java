package test.java.filemanager;

import board.Board;
import board.Cell;
import board.PieceFactory;
import fileManager.FileManager;
import org.junit.jupiter.api.*;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileManagerTest {

    private FileManager fileManager;

    // 테스트용 초기 보드 (P가 한 칸 전진한 상태)
    private Board createTestBoard() {
        Board board = new Board();
        board.initializeBoard(); // 기본 보드 생성
        Cell from = board.getCell(6, 4); // e2 (P)
        Cell to = board.getCell(5, 4);   // e3
        to.setPiece(from.getPiece());
        from.setPiece(null);
        return board;
    }

    @BeforeEach
    public void setUp() {
        fileManager = FileManager.getInstance();
        fileManager.setCurrentBoard(createTestBoard());
    }

    @AfterEach
    public void cleanUp() {
        for (int i = 1; i <= 5; i++) {
            File file = new File("saves/savefile" + i + ".txt");
            if (file.exists()) file.delete();
        }
    }

    @Test
    @Order(1)
    public void testSingletonInstance() {
        FileManager instance1 = FileManager.getInstance();
        FileManager instance2 = FileManager.getInstance();
        assertSame(instance1, instance2, "FileManager는 싱글턴이어야 함");
    }

    @Test
    @Order(2)
    public void testSaveFilesInAllSlots() {
        for (int i = 1; i <= 5; i++) {
            boolean result = fileManager.overWriteSavedFile(i);
            assertTrue(result, "슬롯 " + i + " 저장에 실패함");
        }

        for (int i = 1; i <= 5; i++) {
            File file = new File("saves/savefile" + i + ".txt");
            assertTrue(file.exists(), "슬롯 " + i + " 파일이 존재하지 않음");
        }
    }

    @Test
    @Order(3)
    public void testLoadSavedFile() {
        // 1. 저장용 보드 준비
        Board board = new Board();
        Cell from = board.getCell(6, 4); // E2
        Cell to = board.getCell(5, 4);   // E3
        to.setPiece(from.getPiece());
        from.setPiece(null);

        fileManager.setCurrentBoard(board);
        boolean saved = fileManager.overWriteSavedFile(1);
        assertTrue(saved, "슬롯 1 저장 실패");

        // 2. 보드 초기화
        Board emptyBoard = new Board();
        fileManager.setCurrentBoard(emptyBoard);

        // 3. 불러오기
        boolean loaded = fileManager.loadSavedFile(1);
        assertTrue(loaded, "슬롯 1에서 불러오기에 실패함");

        // 4. 불러온 보드 확인
        Board loadedBoard = fileManager.getCurrentBoard();
        assertNotNull(loadedBoard.getCell(5, 4).getPiece(), "e3에 기물이 없음");
        assertEquals("P", loadedBoard.getCell(5, 4).getPiece().getSymbol(), "불러온 보드에 P가 e3에 없음");
    }

    @Test
    @Order(4)
    public void testDeleteSavedFile() {
        fileManager.overWriteSavedFile(2);
        File file = new File("saves/savefile2.txt");
        assertTrue(file.exists(), "삭제 전 파일이 존재해야 함");

        boolean deleted = fileManager.deleteSavedFile(2);
        assertTrue(deleted, "슬롯 2 삭제 실패");

        assertFalse(file.exists(), "파일이 삭제되지 않음");
    }

    @Test
    @Order(5)
    public void testInvalidSlotOperations() {
        assertFalse(fileManager.overWriteSavedFile(0), "0번 슬롯 저장은 실패해야 함");
        assertFalse(fileManager.loadSavedFile(6), "6번 슬롯 로드는 실패해야 함");
        assertFalse(fileManager.deleteSavedFile(-1), "음수 슬롯 삭제는 실패해야 함");
    }
}