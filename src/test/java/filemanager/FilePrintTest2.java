package test.java.filemanager;

import fileManager.FileManager;
import fileManager.FilePrint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class FilePrintTest2 {
    private static final int MAX_SLOT = 5;
    private static final int SAVE_ITERATIONS = 10;

    private final FileManager fileManager = FileManager.getInstance();
    private final Deque<Integer> savedSlotsStack = new ArrayDeque<>();
    private final Random random = new Random();

    @BeforeEach
    public void setup() {
        fileManager.clearMoveHistory();
        for (int i = 0; i < 15; i++) {
            fileManager.addHistory("MOVE_" + i);
        }
    }

    @Test
    public void testSaveThenDeleteInReverseOrder() {
        System.out.println("\n===== START SAVE PHASE =====");

        // 저장 10회 수행, 저장된 슬롯을 Stack에 push
        for (int i = 0; i < SAVE_ITERATIONS; i++) {
            int slot = random.nextInt(MAX_SLOT) + 1;
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

        // moveHistory는 그대로 유지됨
        assertEquals(15, fileManager.getMoveHistory().size());

        // FileManager는 싱글턴
        int hash1 = System.identityHashCode(FileManager.getInstance());
        int hash2 = System.identityHashCode(fileManager);
        assertEquals(hash1, hash2);
    }
}
