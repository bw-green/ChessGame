package test.java.file;

import board.Board;
import fileManager.SaveIntegrityChecker;
import org.junit.jupiter.api.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SaveIntegrityChecker 단위 테스트 – 정상 시나리오 + 각 메서드별 오류 시나리오
 * <p>
 * ✔ JUnit 5 & reflection 사용 (private 메서드 호출)
 * ✔ lines 리스트를 매번 새로 만들어 주입 → 메서드별 고장 케이스 확인
 */
class SaveIntegrityCheckerTest {

    /* -------------------------------------------------- 헬퍼 -------------------------------------------------- */

    /** 다중라인 문자열(\n 구분)을 List<String>으로 변환 */
    private static List<String> toLines(String raw) {
        return Arrays.asList(raw.split("\\n"));
    }

    /** reflection 으로 boolean 반환 private 메서드 호출 */
    private static boolean invokeBool(SaveIntegrityChecker c, String name, Class<?>[] types, Object... args) {
        try {
            Method m = SaveIntegrityChecker.class.getDeclaredMethod(name, types);
            m.setAccessible(true);
            return (boolean) m.invoke(c, args);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /** reflection 으로 int 반환 private 메서드 호출 */
    private static int invokeInt(SaveIntegrityChecker c, String name, Class<?>[] types, Object... args) {
        try {
            Method m = SaveIntegrityChecker.class.getDeclaredMethod(name, types);
            m.setAccessible(true);
            return (int) m.invoke(c, args);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /* -------------------------------------------------- 공통 정상 lines -------------------------------------------------- */
    private static final String NORMAL_SAVE = String.join("\n",
            "id:Guest,",
            "save_name:Nfz9jK6EES,",
            "game_type:1,",
            "castling:1,",
            "promotion:1,",
            "enpassant:1,",
            "ThreeCheckW:-1,",
            "ThreeCheckB:-1,",
            "board:",
            "black",
            "P 4 4",
            "r n b q k b n r ",
            "p p p p p p p p ",
            ". . . . . . . . ",
            ". . . . . . . . ",
            ". . . . P . . . ",
            ". . . . . . . . ",
            "P P P P . P P P ",
            "R N B Q K B N R ");

    /* -------------------------------------------------- 1. KeyValueBlock -------------------------------------------------- */

    @Test
    void keyValueBlock_shouldPass_withNormalSave() {
        SaveIntegrityChecker c = new SaveIntegrityChecker(toLines(NORMAL_SAVE));
        assertTrue(invokeBool(c, "checkKeyValueBlock", new Class[]{}));
        assertEquals(0, c.getErrors().size());
    }

    @Test
    void keyValueBlock_shouldFail_whenMissingRequiredKey() {
        String bad = NORMAL_SAVE.replace("game_type:1,\n", ""); // game_type 제거
        SaveIntegrityChecker c = new SaveIntegrityChecker(toLines(bad));
        assertFalse(invokeBool(c, "checkKeyValueBlock", new Class[]{}));
        assertFalse(c.getErrors().isEmpty());
    }

    @Test
    void keyValueBlock_shouldFail_whenInvalidValue() {
        String bad = NORMAL_SAVE.replace("game_type:1,", "game_type:100,");
        SaveIntegrityChecker c = new SaveIntegrityChecker(toLines(bad));
        assertFalse(invokeBool(c, "checkKeyValueBlock", new Class[]{}));
    }

    /* -------------------------------------------------- 2. findBoardStartIndex -------------------------------------------------- */
    @Test
    void boardStartIndex_shouldDetectProperStart() {
        SaveIntegrityChecker c = new SaveIntegrityChecker(toLines(NORMAL_SAVE));
        int idx = invokeInt(c, "findBoardStartIndex", new Class[]{});
        assertEquals(11, idx); // 정상 위치
    }

    @Test
    void boardStartIndex_shouldFail_whenNo8x8Block() {
        String bad = NORMAL_SAVE.replace("r n b q k b n r ", "r n b q k"); // 첫 보드라인 5토큰으로 깨뜨림
        SaveIntegrityChecker c = new SaveIntegrityChecker(toLines(bad));
        int idx = invokeInt(c, "findBoardStartIndex", new Class[]{});
        assertEquals(-1, idx);
    }

    /* -------------------------------------------------- 3. checkBoardLines -------------------------------------------------- */
    @Test
    void boardLines_shouldFail_whenTurnLineInvalid() {
        String bad = NORMAL_SAVE.replace("black", "purple");
        SaveIntegrityChecker c = new SaveIntegrityChecker(toLines(bad));
        int start = invokeInt(c, "findBoardStartIndex", new Class[]{});
        assertFalse(invokeBool(c, "checkBoardLines", new Class[]{int.class}, start));
    }

    /* -------------------------------------------------- 4. checkPieceSymbols -------------------------------------------------- */
    @Test
    void pieceSymbols_shouldFail_whenUnexpectedSymbol() {
        String bad = NORMAL_SAVE.replace("p p p p p p p p", "x p p p p p p p");
        SaveIntegrityChecker c = new SaveIntegrityChecker(toLines(bad));
        // 전처리 (헤더 / 보드라인 등록)
        invokeBool(c, "checkKeyValueBlock", new Class[]{});
        int start = invokeInt(c, "findBoardStartIndex", new Class[]{});
        invokeBool(c, "checkBoardLines", new Class[]{int.class}, start);
        assertFalse(invokeBool(c, "checkPieceSymbols", new Class[]{}));
    }

    /* -------------------------------------------------- 5. checkKingCount -------------------------------------------------- */
    @Test
    void kingCount_shouldFail_whenTwoWhiteKings() {
        String bad = NORMAL_SAVE.replace("K B N R", "K K N R"); // 두 번째 K 삽입
        SaveIntegrityChecker c = new SaveIntegrityChecker(toLines(bad));
        invokeBool(c, "checkKeyValueBlock", new Class[]{});
        int start = invokeInt(c, "findBoardStartIndex", new Class[]{});
        invokeBool(c, "checkBoardLines", new Class[]{int.class}, start);
        assertFalse(invokeBool(c, "checkKingCount", new Class[]{}));
    }

    /* -------------------------------------------------- 6. checkRuleFlags -------------------------------------------------- */
    @Test
    void ruleFlags_shouldFail_whenNonBinary() {
        String bad = NORMAL_SAVE.replace("castling:1,", "castling:5,");
        SaveIntegrityChecker c = new SaveIntegrityChecker(toLines(bad));
        invokeBool(c, "checkKeyValueBlock", new Class[]{});
        assertFalse(invokeBool(c, "checkRuleFlags", new Class[]{}));
    }

    /* -------------------------------------------------- 7. checkThreeCheckSettings -------------------------------------------------- */
    @Test
    void threeCheckSettings_shouldFail_whenInvalidForType1() {
        String bad = NORMAL_SAVE.replace("ThreeCheckW:-1", "ThreeCheckW:2");
        SaveIntegrityChecker c = new SaveIntegrityChecker(toLines(bad));
        invokeBool(c, "checkKeyValueBlock", new Class[]{});
        assertFalse(invokeBool(c, "checkThreeCheckSettings", new Class[]{}));
    }

    /* -------------------------------------------------- 8. checkPieceCoordinates -------------------------------------------------- */
    @Test
    void pieceCoordinates_shouldFail_whenCoordinateMismatch() {
        String bad = NORMAL_SAVE.replace("P 4 4", "P 0 0"); // (0,0)은 흑 룩 위치
        SaveIntegrityChecker c = new SaveIntegrityChecker(toLines(bad));
        invokeBool(c, "checkKeyValueBlock", new Class[]{});
        int start = invokeInt(c, "findBoardStartIndex", new Class[]{});
        invokeBool(c, "checkBoardLines", new Class[]{int.class}, start);
        assertFalse(invokeBool(c, "checkPieceCoordinates", new Class[]{}));
    }

    /* -------------------------------------------------- 9. validateFile 전체 -------------------------------------------------- */
    @Test
    void validateFile_shouldThrowNPE_whenIntegrityFails() {
        String bad = NORMAL_SAVE.replace("p p p p p p p p", "p p p p X p p p");
        SaveIntegrityChecker c = new SaveIntegrityChecker(toLines(bad));
        assertThrows(NullPointerException.class, c::validateFile);
        assertFalse(c.getErrors().isEmpty());
    }

    @Test
    void validateFile_shouldReturnBoard_whenEverythingOk() {
        SaveIntegrityChecker c = new SaveIntegrityChecker(toLines(NORMAL_SAVE));
        Board b = c.validateFile();
        assertNotNull(b);
        assertTrue(c.getErrors().isEmpty());
    }
}
