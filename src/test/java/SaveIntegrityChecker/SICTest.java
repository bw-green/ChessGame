package test.java.SaveIntegrityChecker;

import fileManager.SaveIntegrityChecker;
import board.Board;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

public class SICTest {

    @Test
    public void testValidSaveFile() {
        List<String> lines = List.of(
                "id: test123",
                "save_name: save123456",
                "game_type: 1",
                "castling: 1",
                "promotion: 1",
                "enpassant: 0",
                "ThreeCheckW: -1",
                "ThreeCheckB: -1",
                "board:",
                "white",
                "r n b q k b n r",
                "p p p p p p p p",
                ". . . . . . . .",
                ". . . . . . . .",
                ". . . . . . . .",
                ". . . . . . . .",
                "P P P P P P P P",
                "R N B Q K B N R"
        );

        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        Board board = checker.validateFile();
        for (String error : checker.getErrors()) {
            System.out.println(error);
        }
        assertNotNull(board, "Board should be created for a valid save file.");
        assertTrue(checker.getErrors().isEmpty(), "No errors should be reported for a valid save file.");
    }

    @Test
    public void testMissingRequiredKey() {
        List<String> lines = List.of(
                "id:test123",
                "save_name:save123456",
                "game_type:1",                // ← 이 부분이 핵심 (value 없음)
                "castling:0",
                "promotion:",
                "enpassant:0",
                "ThreeCheckW:0",
                "ThreeCheckB:0",
                "board:",
                "white",
                "r n b q k b n r",
                "p p p p p p p p",
                ". . . . . . . .",
                ". . . . . . . .",
                ". . . . . . . .",
                ". . . . . . . .",
                "P P P P P P P P",
                "R N B Q K B N R"
        );

        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        boolean result = checker.testCheckKeyValueBlock();  // game_type 값 없음 검출 예상

        System.out.println("Case 1 - Missing game_type value");
        for (String error : checker.getErrors()) {
            System.out.println("    " + error);
        }
        System.out.println();

        assertFalse(result, "Missing 'game_type' value should fail checkKeyValueBlock.");
    }


    @Test
    public void MissingKeyValue(){
        List<String> lines = List.of(
                "id: test123",
                "save_name: save123456",
                "castling: 1",
                "promotion: 1",
                "enpassant: 0",
                "ThreeCheckW: -1",
                "ThreeCheckB: -1",
                "board:",
                "white",
                "r n b q k b n r",
                "p p p p p p p p",
                ". . . . . . . .",
                ". . . . . . . .",
                ". . . . . . . .",
                ". . . . . . . .",
                "P P P P P P P P",
                "R N B Q K B N R"
        );

        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        boolean result = checker.testCheckKeyValueBlock();  // game_type 값 없음 검출 예상

        System.out.println("Case 2 - Gametype missing");
        for (String error : checker.getErrors()) {
            System.out.println("    " + error);
        }
        System.out.println();

        assertFalse(result, "Missing 'game_type' value should fail checkKeyValueBlock.");
    }

    @Test
    public void testInvalidFormatMissingColon() {
        List<String> lines = List.of(
                "id test123",                   // ← 콜론 누락
                "save_name:save123456",
                "game_type:1",
                "castling:1",
                "promotion:0",
                "enpassant:0",
                "ThreeCheckW:0",
                "ThreeCheckB:0",
                "board:"
        );

        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        boolean result = checker.testCheckKeyValueBlock();

        System.out.println("Case - Missing colon");
        for (String error : checker.getErrors()) {
            System.out.println("    " + error);
        }

        assertFalse(result, "Missing colon should trigger format error.");
    }

    @Test
    public void InvalidKeyValueFormat(){ //오류 메시지 엉켜서 나옴
        List<String> lines = List.of(
                "id-test123",                      // ← 잘못된 형식 (콜론이 아님)
                "save_name:save123456",
                "game_type:1",
                "castling:0",
                "promotion:0",
                "enpassant:0",
                "ThreeCheckW:0",
                "ThreeCheckB:0",
                "board:",
                "white",
                "r n b q k b n r",
                "p p p p p p p p",
                ". . . . . . . .",
                ". . . . . . . .",
                ". . . . . . . .",
                ". . . . . . . .",
                "P P P P P P P P",
                "R N B Q K B N R"
        );

        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        boolean result = checker.testCheckKeyValueBlock();  // 이 함수가 false여야 정상

        System.out.println("Case - Invalid key-value format");
        for (String error : checker.getErrors()) {
            System.out.println("    " + error);
        }
        System.out.println();

        assertFalse(result, "Invalid key-value format should fail checkKeyValueBlock.");
    }

    @Test
    public void keyDisorder(){
        List<String> lines = List.of(
                "id: test123",
                "save_name: save123456",
                "game_type: 1",
                "promotion: 1", //promotion and castling disorder
                "castling: 1",
                "enpassant: 0",
                "ThreeCheckW: -1",
                "ThreeCheckB: -1",
                "board:",
                "white",
                "r n b q k b n r",
                "p p p p p p p p",
                ". . . . . . . .",
                ". . . . . . . .",
                ". . . . . . . .",
                ". . . . . . . .",
                "P P P P P P P P",
                "R N B Q K B N R"
        );

        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        boolean result = checker.testCheckKeyValueBlock();  // game_type 값 없음 검출 예상

        System.out.println("Case  - promotion and castling changed order");
        for (String error : checker.getErrors()) {
            System.out.println("    " + error);
        }
        System.out.println();

        assertFalse(result, "Missing 'game_type' value should fail checkKeyValueBlock.");
    }

    @Test
    public void BoardLineLoss(){
        List<String> lines = List.of(
                "id: test123",
                "save_name: save123456",
                "game_type: 1",
                "promotion: 1",
                "castling: 1",
                "enpassant: 0",
                "ThreeCheckW: -1",
                "ThreeCheckB: -1",
                "board:",
                "white",
                "r n b q k b n r",
                "p p p p p p p p",
                ". . . . . . . .",      //line 1줄 삭제
                ". . . . . . . .",
                ". . . . . . . .",
                "P P P P P P P P",
                "R N B Q K B N R"
        );

        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        boolean result = checker.testCheckBoardLines(9);  // game_type 값 없음 검출 예상

        System.out.println("Case  - not enough board line");
        for (String error : checker.getErrors()) {
            System.out.println("    " + error);
        }
        System.out.println();

        assertFalse(result, "Missing line for 8×8 board");
    }

    @Test
    public void TurnIndicatorMissing(){
        List<String> lines = List.of(
                "id: test123",
                "save_name: save123456",
                "game_type: 1",
                "promotion: 1",
                "castling: 1",
                "enpassant: 0",
                "ThreeCheckW: -1",
                "ThreeCheckB: -1",
                "board:",
                "",     // no turn indicator
                "r n b q k b n r",
                "p p p p p p p p",
                ". . . . . . . .",
                ". . . . . . . .",
                ". . . . . . . .",
                "P P P P P P P P",
                "R N B Q K B N R"
        );

        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        boolean result = checker.testCheckBoardLines(9);  // game_type 값 없음 검출 예상

        System.out.println("Case  - No Turn Indicator");
        for (String error : checker.getErrors()) {
            System.out.println("    " + error);
        }
        System.out.println();

        assertFalse(result, "No Turn Indicator");
    }

    @Test
    public void testInvalidTurnIndicator() {
        List<String> lines = List.of(
                "id: test123",
                "save_name: save123456",
                "game_type: 1",
                "castling: 1",
                "promotion: 1",
                "enpassant: 0",
                "ThreeCheckW: -1",
                "ThreeCheckB: -1",
                "board:",
                "blue", // Invalid turn indicator
                "r n b q k b n r",
                "p p p p p p p p",
                ". . . . . . . .",
                ". . . . . . . .",
                ". . . . . . . .",
                ". . . . . . . .",
                "P P P P P P P P",
                "R N B Q K B N R"
        );

        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        assertNull(checker.validateFile());
        for (String error : checker.getErrors()) {
            System.out.println(error);
        }
        assertTrue(checker.getErrors().toString().contains("Invalid turn indicator"));
    }
}
