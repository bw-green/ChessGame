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
                "save_name: save1",
                "game_type: 1",
                "castling: 1",
                "promotion: 1",
                "enpassant: 0",
                "threeCheckW: -1",
                "threeCheckB: -1",
                "board:",
                "white",
                // 특수좌표 없음
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

        assertNotNull(board, "Board should be created for a valid save file.");
        assertTrue(checker.getErrors().isEmpty(), "No errors should be reported for a valid save file.");
    }












    /**
     * 테스트 목적: checkPieceSymbols() 함수에서 다양한 잘못된 기물 기호가
     * 있는 경우 오류 메시지가 발생하고 Board 생성이 실패해야 한다.
     * 케이스: game_type=1 기본 체스에서 허용되지 않은 기호 (X, Z, @ 등)를
     * 사용하는 경우 오류 발생 기대.
     */
    @ParameterizedTest
    @ValueSource(strings = {"X", "Z", "@", "1", "#"})
    void testInvalidPieceSymbols(String invalidSymbol) {
        List<String> lines = List.of(
                "id: test123",
                "save_name: save1",
                "game_type: 1",
                "castling: 1",
                "promotion: 1",
                "enpassant: 0",
                "threeCheckW: -1",
                "threeCheckB: -1",
                "board:",
                "white",
                // 특수좌표 없음
                "r n b q k b n r",
                "p p p p p p p p",
                ". . . . . . . .",
                ". . . . . . . .",
                ". . . . . . . .",
                ". . . . . . . .",
                "P P P P P P P P",
                "R N B Q " + invalidSymbol + " K B N R"
        );

        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        Board board = checker.validateFile();

        assertNull(board, "Board should be null when invalid piece symbol '" + invalidSymbol + "' is present.");
        assertTrue(checker.getErrors().stream().anyMatch(msg -> msg.contains("Invalid piece symbol")),
                "Error message should report invalid piece symbol '" + invalidSymbol + "'.");
    }

    /**
     * 차투랑가 테스트
     */
    @ParameterizedTest
    @ValueSource(strings = {"Q", "P", "p", "q", "X", "@", "1", "#"})
    void testInvalidPieceSymbols_Chaturanga(String invalidSymbol) {
        List<String> lines = List.of(
                "id: test123",
                "save_name: save1",
                "game_type: 3", // 차투랑가
                "castling: 0", // 차투랑가는 고정 false/false/true
                "promotion: 1",
                "enpassant: 0",
                "threeCheckW: -1",
                "threeCheckB: -1",
                "board:",
                "white",
                // 특수좌표 없음
                "r n b q k b n r", // 위에는 허용 안 되는 기호 넣지 않고
                "r r r r r r r r",
                ". . . . . . . .",
                ". . . . . . . .",
                ". . . . . . . .",
                ". . . . . . . .",
                "R R R R R R R R",
                "R N B Q " + invalidSymbol + " K B N R" // 여기에 invalid symbol 넣음
        );

        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        Board board = checker.validateFile();

        assertNull(board, "Board should be null when invalid piece symbol '" + invalidSymbol + "' is present (Chaturanga).");
        assertTrue(checker.getErrors().stream().anyMatch(msg -> msg.contains("Invalid piece symbol")),
                "Error message should report invalid piece symbol '" + invalidSymbol + "' (Chaturanga).");
    }


    @ParameterizedTest
    @CsvSource({
            "1,1,false",  // 정상 케이스 → Board 생성됨
            "0,1,true",   // K 부족 → 오류 발생
            "1,0,true",   // k 부족 → 오류 발생
            "2,1,true",   // K 초과 → 오류 발생
            "1,2,true",   // k 초과 → 오류 발생
            "0,0,true"    // K/k 둘 다 부족 → 오류 발생
    })
    void testKingCount(int whiteKingCount, int blackKingCount, boolean expectError) {
        // 보드 라인 구성
        String whiteKingRow = whiteKingCount >= 1 ? "K . . . . . . ." : ". . . . . . . .";
        if (whiteKingCount == 2) whiteKingRow = "K K . . . . . .";

        String blackKingRow = blackKingCount >= 1 ? "k . . . . . . ." : ". . . . . . . .";
        if (blackKingCount == 2) blackKingRow = "k k . . . . . .";

        List<String> lines = List.of(
                "id: test123",
                "save_name: save1",
                "game_type: 1",
                "castling: 1",
                "promotion: 1",
                "enpassant: 0",
                "threeCheckW: -1",
                "threeCheckB: -1",
                "board:",
                "white",
                "r n b q r b n r",
                "p p p p p p p p",
                ". . . . . . . .",
                ". . . . . . . .",
                ". . . . . . . .",
                blackKingRow,
                "P P P P P P P P",
                whiteKingRow
        );

        SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
        Board board = checker.validateFile();

        if (expectError) {
            assertNull(board, "Board should be null when king count is invalid (K=" + whiteKingCount + ", k=" + blackKingCount + ").");
            assertTrue(checker.getErrors().stream().anyMatch(msg -> msg.contains("Invalid number of white kings") || msg.contains("Invalid number of black kings")),
                    "Error message should report invalid king count (K=" + whiteKingCount + ", k=" + blackKingCount + ").");
        } else {
            assertNotNull(board, "Board should be created when king count is valid.");
            assertTrue(checker.getErrors().isEmpty(), "No errors should be reported for valid king count.");
        }
    }


}
