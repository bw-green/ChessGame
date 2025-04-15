package test.java.filemanager;

import board.Board;
import board.Cell;
import fileManager.FileManager;
import fileManager.FilePrint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class FilePrintTest_LFSfocus {
    private static final int MAX_SLOT = 5;
    private static final int SAVE_ITERATIONS = 10;

    private final FileManager fileManager = FileManager.getInstance();
    private final Deque<Integer> savedSlotsStack = new ArrayDeque<>();
    private final Random random = new Random();

    private Board createSampleBoard(int version) {
        Board board = new Board();
        // 기보용 데이터: E2 → E3로 Pawn 이동 (좌표는 row=6, col=4 → row=5, col=4)
        if (version % 2 == 0) { // Even 버전은 기본 상태
            return board;
        } else { // Odd 버전은 Pawn 이동 적용
            Cell from = board.getCell(6, 4); // E2
            Cell to = board.getCell(5, 4);   // E3
            to.setPiece(from.getPiece());
            from.setPiece(null);
            return board;
        }
    }

    @BeforeEach
    public void setup() {
        // 이전 파일 정리
        for (int i = 1; i <= MAX_SLOT; i++) {
            File file = new File("saves/savefile" + i + ".txt");
            if (file.exists()) file.delete();
        }
    }

    @Test
    public void testSaveThenDeleteInReverseOrder() {
        System.out.println("\n===== START SAVE PHASE =====");

        // 저장 10회 수행, 저장된 슬롯을 Stack에 push
        for (int i = 0; i < SAVE_ITERATIONS; i++) {
            int slot = random.nextInt(MAX_SLOT) + 1;
            fileManager.setCurrentBoard(createSampleBoard(i));
            FilePrint printer = new FilePrint(FileManager.getInstance());
            boolean success = fileManager.overWriteSavedFile(slot);
            if (success) {
                savedSlotsStack.push(slot);
                System.out.println("Saved to slot: " + slot);
            }
        }

        System.out.println("\n===== START DELETE PHASE =====");

        // 저장된 순서의 역순으로 삭제
        while (!savedSlotsStack.isEmpty()) {
            int slot = savedSlotsStack.pop();
            FilePrint printer = new FilePrint(FileManager.getInstance());
            System.out.println("Deleting slot: " + slot);
            printer.deleteFilePrint(slot);
        }

        System.out.println("\n===== VERIFY STATE =====");

        // filename 상태 검증: 모두 NO DATA
        for (int i = 1; i <= MAX_SLOT; i++) {
            assertEquals("NO DATA", fileManager.getFilename().get(i - 1));
        }

        // 현재 board는 마지막 저장된 상태 유지 (삭제와 무관)
        Board current = fileManager.getCurrentBoard();
        assertNotNull(current);
        // E3에는 기물이 있고 E2는 비어 있어야 함 (홀수 번째 저장일 경우)
        assertNotNull(current.getCell(5, 4).getPiece());
        assertNull(current.getCell(6, 4).getPiece());

        // FileManager 싱글턴 확인
        int hash1 = System.identityHashCode(FileManager.getInstance());
        int hash2 = System.identityHashCode(fileManager);
        assertEquals(hash1, hash2);
    }
}
