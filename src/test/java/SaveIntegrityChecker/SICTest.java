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

}
