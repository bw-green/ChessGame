package test.java.filemanager;

import fileManager.FileManager;
import org.junit.jupiter.api.*;
import java.util.List;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest { //FileManager Test
    private FileManager fileManager;
    //!!saves 폴더 자동생성됩니다.!! 폴더 삭제하는건 따로 구현 안했습니다. 회의때 논의하면 좋을 것 같습니다.
    @BeforeEach
    void setUp() {
        fileManager = new FileManager();
        fileManager.getMoveHistory().clear(); // 초기화된 moveHistory와는 다른 복사본이지만, 테스트용 클린업 습관
    }

    @Test
    void testOverwriteAndLoadSavedFile() {

        // 준비: 움직임을 수동으로 추가
        fileManager.addHistory("P A2 A4");
        fileManager.addHistory("k B1 C3");

        // 저장
        boolean saveResult = fileManager.overWriteSavedFile(1);
        assertTrue(saveResult, "파일 저장에 실패했습니다.");

        // 로드
        boolean loadResult = fileManager.loadSavedFile(1);
        assertTrue(loadResult, "파일 로딩에 실패했습니다.");

        // 검증
        List<String> loadedMoves = fileManager.getMoveHistory();
        assertEquals(2, loadedMoves.size());
        assertEquals("P A2 A4", loadedMoves.get(0));
        assertEquals("k B1 C3", loadedMoves.get(1));
    }

    @Test
    void testInvalidSlotSave() {
        boolean result = fileManager.overWriteSavedFile(6); // 1~5 이외
        assertFalse(result, "1~5 범위를 벗어난 슬롯 저장은 실패해야 합니다.");
    }

    @Test
    void testInvalidSlotLoad() {
        boolean result = fileManager.loadSavedFile(0); // 잘못된 슬롯
        assertFalse(result, "1~5 범위를 벗어난 슬롯 로딩은 실패해야 합니다.");
    }

    /*슬롯 자리에 문자열 등 아예 벗어나는 값이 들어가는 경우는 GameManager
    에서 처리해야 되는 오류라 생각해서<("Wrong number")-인자 오류 부분> 단순한 오류 식별 능력이 있는지만 봤습니다.
    아마 추후 slot부분은 완성된 입력이 들어온다 가정하여, 위 테스트 부분은 필요없는 테스트 부분이 될 것 같습니다.*/
    @AfterEach
    void tearDown() {
        // 테스트 후 파일 삭제 (clean-up)
        for (int i = 1; i <= 5; i++) {
            File file = new File("saves/savefile" + i + ".txt");
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
