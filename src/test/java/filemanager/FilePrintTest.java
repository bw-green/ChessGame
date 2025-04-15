package test.java.filemanager;

import board.Board;
import board.Cell;
import fileManager.FileManager;
import fileManager.FilePrint;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FilePrintTest {

    private Board createSampleBoard() {
        Board board = new Board();
        // P가 한 칸 앞으로 이동 (예: E2 -> E3)
        Cell original = board.getCell(6, 4); // E2
        Cell target = board.getCell(5, 4);   // E3
        target.setPiece(original.getPiece());
        original.setPiece(null);
        return board;
    }

    @Test
    void integratedFileManagerTest() {
        FileManager fileManager = FileManager.getInstance();
        FilePrint filePrint = new FilePrint(fileManager);

        System.out.println("========= 통합 테스트 시작 =========");

        // 1. 이전 파일 정리
        for (int i = 1; i <= 5; i++) {
            File file = new File("saves/savefile" + i + ".txt");
            if (file.exists()) file.delete();
        }

        // 2. 각 슬롯에 보드 저장
        for (int i = 1; i <= 5; i++) {
            fileManager.setCurrentBoard(createSampleBoard());
            filePrint.saveFilePrint(i);
        }

        // 3. 3번 슬롯 불러오기
        filePrint.loadFilePrint(3);
        Board loadedBoard = fileManager.getClass().cast(fileManager).getClass().cast(fileManager).getClass().cast(fileManager).getClass().cast(fileManager).getCurrentBoard();

        assertNotNull(loadedBoard);
        assertNotNull(loadedBoard.getCell(5, 4).getPiece()); // E3
        assertNull(loadedBoard.getCell(6, 4).getPiece()); // E2

        // 4. 싱글턴 확인
        FileManager fileManager2 = FileManager.getInstance();
        assertSame(fileManager, fileManager2);

        // 5. 5번 슬롯 삭제
        filePrint.deleteFilePrint(5);
        File deletedFile = new File("saves/savefile5.txt");
        assertFalse(deletedFile.exists());

        // 빈 슬롯 delete 테스트
        filePrint.deleteFilePrint(5);

        // "/savefile 테스트
        filePrint.saveListPrint();

        // 6. 다시 저장하고 불러오기 검증
        fileManager.setCurrentBoard(createSampleBoard());
        boolean saved = fileManager.overWriteSavedFile(2);
        assertTrue(saved);

        boolean loaded = fileManager.loadSavedFile(2);
        assertTrue(loaded);

        Board reloadedBoard = fileManager.getCurrentBoard();
        assertNotNull(reloadedBoard);
        assertNotNull(reloadedBoard.getCell(5, 4).getPiece());
        assertNull(reloadedBoard.getCell(6, 4).getPiece());

        // 7. 정리
        for (int i = 1; i <= 5; i++) {
            File file = new File("saves/savefile" + i + ".txt");
            if (file.exists()) {
                boolean deleted = file.delete();
                assertTrue(deleted, "파일 삭제 실패: " + file.getName());
            }
        }

        System.out.println("========= 통합 테스트 끝 =========");
    }
}