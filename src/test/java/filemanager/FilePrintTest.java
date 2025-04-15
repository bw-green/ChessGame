package test.java.filemanager;

import fileManager.FileManager;
import fileManager.FilePrint;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FilePrintTest { //출력 FilePrint Class 테스트

    @Test
    void integratedFileManagerTest() {
        FileManager fileManager = new FileManager();
        FilePrint filePrint = new FilePrint(fileManager);

        System.out.println("========= 통합 테스트 시작 =========");

        // 1. 이전 파일 정리
        for (int i = 1; i <= 5; i++) {
            File file = new File("saves/savefile" + i + ".txt");
            if (file.exists()) file.delete();
        }

        fileManager.clearMoveHistory();
        assertEquals(0, fileManager.getMoveHistory().size());

        // 2. 각 슬롯에 기보 저장 (5개)
        for (int i = 1; i <= 5; i++) {
            fileManager.addHistory("P E2 E4");
            fileManager.addHistory("k B1 C3");
            filePrint.saveFilePrint(i);
            fileManager.clearMoveHistory();
        }

        // 3. 3번 슬롯 불러오기
        filePrint.loadFilePrint(3);
        List<String> historyAfterLoad = fileManager.getMoveHistory();
        assertEquals(2, historyAfterLoad.size());
        assertEquals("P E2 E4", historyAfterLoad.get(0));

        FileManager fileManager2 = new FileManager();
        FilePrint filePrint2 = new FilePrint(fileManager2);
        // 4. 5번 슬롯 삭제
        filePrint2.deleteFilePrint(5);
        File deletedFile = new File("saves/savefile5.txt");
        assertFalse(deletedFile.exists(), "5번 파일이 실제로 삭제되어야 함");

        //빈 슬롯 delete 테스트
        filePrint2.deleteFilePrint(5);

        // "/savefile 테스트
        filePrint2.saveListPrint();

        // 5. 대용량 moveHistory 작성 (150줄)
        fileManager2.clearMoveHistory();
        String[] sampleMoves = {
                "P E2 E4", "P E7 E5", "N G1 F3", "N B8 C6", "B F1 C4",
                "B F8 C5", "Q D1 H5", "N G8 F6", "Q H5 F7", "K E8 F7"
        };
        for (int i = 0; i < 15; i++) {
            for (String move : sampleMoves) {
                fileManager2.addHistory(move);
            }
        }
        assertEquals(150, fileManager.getMoveHistory().size());

        // 6. 2번 슬롯에 저장 → clear → 로드 → 재검증
        boolean saved = fileManager.overWriteSavedFile(2);
        assertTrue(saved);

        fileManager.clearMoveHistory();
        assertEquals(0, fileManager.getMoveHistory().size());

        boolean loaded = fileManager.loadSavedFile(2);
        assertTrue(loaded);

        assertEquals(150, fileManager.getMoveHistory().size());
        assertEquals("P E2 E4", fileManager.getMoveHistory().get(0));

        // 테스트 종료 후 저장파일 삭제
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