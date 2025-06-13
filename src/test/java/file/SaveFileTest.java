package test.java.file;

import fileManager.SaveIntegrityChecker;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SaveFileTest {

    /**
     * [공용] 정상적인 save파일 lines 생성
     */
    private List<String> getValidLines() {
        List<String> lines = new ArrayList<>();
        // ===== 1. key-value 블록 =====
        lines.add("id:Guest,");                   // 0
        lines.add("save_name:Nfz9jK6EES,");       // 1
        lines.add("game_type:1,");                // 2
        lines.add("castling:1,");                 // 3
        lines.add("promotion:1,");                // 4
        lines.add("enpassant:1,");                // 5
        lines.add("ThreeCheckW:-1,");             // 6
        lines.add("ThreeCheckB:-1,");             // 7
        // ===== 2. 보드 =====
        lines.add("board:");                      // 8
        lines.add("black");                       // 9
        lines.add("P 4 4");                       // 10 (특수좌표)
        // ===== 3. 8x8 보드 상태 =====
        lines.add("r n b q k b n r ");            // 11
        lines.add("p p p p p p p p ");            // 12
        lines.add(". . . . . . . . ");            // 13
        lines.add(". . . . . . . . ");            // 14
        lines.add(". . . . P . . . ");            // 15
        lines.add(". . . . . . . . ");            // 16
        lines.add("P P P P . P P P ");            // 17
        lines.add("R N B Q K B N R ");            // 18
        return lines;
    }

    // ---------------------------------------------------------------
    // 1. checkKeyValueBlock
    // ---------------------------------------------------------------

    @Test
    void testCheckKeyValueBlock_valid() {
        List<String> lines = getValidLines();
        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        assertTrue(checker.testCheckKeyValueBlock());
        assertTrue(checker.getErrors().isEmpty());
    }

    @Test
    void testCheckKeyValueBlock_missingKey() {
        List<String> lines = getValidLines();
        // save_name 누락
        lines.remove(1);
        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        assertFalse(checker.testCheckKeyValueBlock());
        assertTrue(checker.getErrors().stream().anyMatch(msg -> msg.contains("Invalid or missing keys")));
    }

    @Test
    void testCheckKeyValueBlock_invalidOrder() {
        List<String> lines = getValidLines();
        // save_name과 game_type 순서 바꿈
        String tmp = lines.get(1);
        lines.set(1, lines.get(2));
        lines.set(2, tmp);
        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        assertFalse(checker.testCheckKeyValueBlock());
        assertTrue(checker.getErrors().stream().anyMatch(msg -> msg.contains("Invalid key order")));
    }

    // ---------------------------------------------------------------
    // 2. checkBoardLines
    // ---------------------------------------------------------------

    @Test
    void testCheckBoardLines_valid() {
        List<String> lines = getValidLines();
        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        // 8x8 보드 시작 인덱스: 11
        assertTrue(checker.testCheckBoardLines(11));
        assertTrue(checker.getErrors().isEmpty());
    }

    @Test
    void testCheckBoardLines_missingBoardKeyword() {
        List<String> lines = getValidLines();
        // "board:" 키워드 누락
        lines.remove(8);
        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        assertFalse(checker.testCheckBoardLines(10));
        assertTrue(checker.getErrors().stream().anyMatch(msg -> msg.contains("board:")));
    }

    @Test
    void testCheckBoardLines_invalidTurn() {
        List<String> lines = getValidLines();
        // turn 정보 오타
        lines.set(9, "블랙");
        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        assertFalse(checker.testCheckBoardLines(11));
        assertTrue(checker.getErrors().stream().anyMatch(msg -> msg.contains("turn indicator")));
    }

    // ---------------------------------------------------------------
    // 3. checkPieceSymbols
    // ---------------------------------------------------------------

    @Test
    void testCheckPieceSymbols_valid() {
        List<String> lines = getValidLines();
        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        checker.testCheckKeyValueBlock();
        checker.testCheckBoardLines(11); // 보드 라인 세팅
        assertTrue(checker.testCheckPieceSymbols());
        assertTrue(checker.getErrors().isEmpty());
    }

    @Test
    void testCheckPieceSymbols_invalidSymbol() {
        List<String> lines = getValidLines();
        // 8x8보드에 허용 안 되는 X 추가
        lines.set(13, ". . . . X . . . ");
        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        checker.testCheckKeyValueBlock();
        checker.testCheckBoardLines(11);
        assertFalse(checker.testCheckPieceSymbols());
        assertTrue(checker.getErrors().stream().anyMatch(msg -> msg.contains("Invalid piece symbol")));
    }

    // ---------------------------------------------------------------
    // 4. checkKingCount
    // ---------------------------------------------------------------

    @Test
    void testCheckKingCount_valid() {
        List<String> lines = getValidLines();
        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        checker.testCheckKeyValueBlock();
        checker.testCheckBoardLines(11);
        checker.testCheckPieceSymbols();
        assertTrue(checker.testCheckKingCount());
        assertTrue(checker.getErrors().isEmpty());
    }

    @Test
    void testCheckKingCount_missingWhiteKing() {
        List<String> lines = getValidLines();
        // 백킹(K) 삭제
        lines.set(18, "R N B Q . B N R ");
        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        checker.testCheckKeyValueBlock();
        checker.testCheckBoardLines(11);
        checker.testCheckPieceSymbols();
        assertFalse(checker.testCheckKingCount());
        assertTrue(checker.getErrors().stream().anyMatch(msg -> msg.contains("white kings")));
    }

    // ---------------------------------------------------------------
    // 5. checkRuleFlags
    // ---------------------------------------------------------------

    @Test
    void testCheckRuleFlags_valid() {
        List<String> lines = getValidLines();
        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        checker.testCheckKeyValueBlock();
        assertTrue(checker.testCheckRuleFlags());
        assertTrue(checker.getErrors().isEmpty());
    }

    @Test
    void testCheckRuleFlags_invalidFlag() {
        List<String> lines = getValidLines();
        // castling 값 이상하게
        lines.set(3, "castling:9,");
        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        checker.testCheckKeyValueBlock();
        assertFalse(checker.testCheckRuleFlags());
        assertTrue(checker.getErrors().stream().anyMatch(msg -> msg.contains("castling")));
    }

    // ---------------------------------------------------------------
    // 6. checkThreeCheckSettings
    // ---------------------------------------------------------------

    @Test
    void testCheckThreeCheckSettings_valid() {
        List<String> lines = getValidLines();
        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        checker.testCheckKeyValueBlock();
        assertTrue(checker.testCheckThreeCheckSettings());
        assertTrue(checker.getErrors().isEmpty());
    }

    @Test
    void testCheckThreeCheckSettings_wrongValue() {
        List<String> lines = getValidLines();
        // ThreeCheckW 값을 0으로 해서 (game_type 1에서 -1만 허용)
        lines.set(6, "ThreeCheckW:0,");
        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        checker.testCheckKeyValueBlock();
        assertFalse(checker.testCheckThreeCheckSettings());
        assertTrue(checker.getErrors().stream().anyMatch(msg -> msg.contains("ThreeCheckW")));
    }

    // ---------------------------------------------------------------
    // 7. checkPieceCoordinates
    // ---------------------------------------------------------------

    @Test
    void testCheckPieceCoordinates_valid() {
        List<String> lines = getValidLines();
        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        checker.testCheckBoardLines(11);
        assertTrue(checker.testCheckPieceCoordinates());
        assertTrue(checker.getErrors().isEmpty());
    }

    @Test
    void testCheckPieceCoordinates_invalidSymbol() {
        List<String> lines = getValidLines();
        // 특수좌표 Pf → Z로 이상하게
        lines.set(10, "Z 4 4");
        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        checker.testCheckKeyValueBlock();
        checker.testCheckBoardLines(11);
        assertFalse(checker.testCheckPieceCoordinates());
        assertFalse(checker.getErrors().stream().anyMatch(msg -> msg.contains("Invalid piece symbol in coordinates")));
    }

    // ---------------------------------------------------------------
    // 8. checkGameEnd (보드 객체 필요. 실제 체스엔진과 연동 필요)
    // 실제로는 보드가 체크메이트 등인 경우를 흉내내야 하지만,
    // 여기선 정상 반환 여부만 단순 체크 예시

    // @Test
    // void testCheckGameEnd_valid() {
    //     List<String> lines = getValidLines();
    //     SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
    //     checker.testCheckBoardLines(11);
    //     checker.testCheckPieceSymbols();
    //     checker.testCheckKingCount();
    //     checker.testCheckKeyValueBlock();
    //     checker.testCheckPieceCoordinates();
    //     Board board = checker.validateFile(); // 실제 보드 생성
    //     if (board != null)
    //         assertTrue(checker.testCheckGameEnd(board));
    // }

    // 보드가 체크메이트 등인 상황에 맞춘 테스트는 실제 Board/Checker/GameEnd 동작에 따라 커스텀 필요
}
