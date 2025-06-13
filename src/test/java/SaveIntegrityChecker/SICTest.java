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
                "id:test123",
                "save_name:save123456",
                "game_type:1",
                "castling:1",
                "promotion:1",
                "enpassant:0",
                "ThreeCheckW:-1",
                "ThreeCheckB:-1",
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
    public void testCheckPieceSymbols() {
        // Case 1 - Valid board
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:1",
                    "castling:1",
                    "promotion:1",
                    "enpassant:0",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
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
            checker.testCheckKeyValueBlock(); // kvMap 정상화
            int boardStartIdx = checker.testFindBoardStartIndex(); // board 시작 인덱스 얻기
            checker.testCheckBoardLines(boardStartIdx); // boardLines 정상화
            checker.getErrors().clear(); // 에러 초기화 후 진짜 테스트
            boolean result = checker.testCheckPieceSymbols();

            System.out.println("Case 1 - Valid board");
            for (String error : checker.getErrors()) {
                System.out.println(error);
            }
            System.out.println();
            assertTrue(result, "Valid board should pass checkPieceSymbols.");
        }

        // Case 2 - Invalid board (unexpected symbol X)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:1",
                    "castling:1",
                    "promotion:1",
                    "enpassant:0",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "r n b q X b n r", // X → invalid
                    "p p p p p p p p",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    "P P P P P P P P",
                    "R N B Q K B N R"
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock(); // kvMap 정상화
            int boardStartIdx = checker.testFindBoardStartIndex(); // board 시작 위치
            checker.testCheckBoardLines(boardStartIdx); // boardLines 정상화
            checker.getErrors().clear(); // 이제 본격 테스트
            boolean result = checker.testCheckPieceSymbols();

            System.out.println("Case 2 - Invalid board (unexpected symbol X)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();
            assertFalse(result, "Invalid board should fail checkPieceSymbols.");
        }

        // Case 3 - Valid board (gameType 3 - Chaturanga)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:3",
                    "castling:0",
                    "promotion:0",
                    "enpassant:0",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "r n g m k g n r",
                    "f f f f f f f f",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    "F F F F F F F F",
                    "R N G M K G N R"
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock(); // kvMap 정상화
            int boardStartIdx = checker.testFindBoardStartIndex(); // board 시작 인덱스 얻기
            checker.testCheckBoardLines(boardStartIdx); // boardLines 정상화
            checker.getErrors().clear(); // 에러 초기화 후 진짜 테스트
            boolean result = checker.testCheckPieceSymbols();

            System.out.println("Case 3 - Valid board (gameType 3 - Chaturanga)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();
            assertTrue(result, "Valid Chaturanga board should pass checkPieceSymbols.");
        }

        // Case 4 - Invalid board (gameType 3 - Chaturanga)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:3",
                    "castling:0",
                    "promotion:0",
                    "enpassant:0",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "r n g m k g n r",
                    "f f f f f f f f",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    "F F F F F F F F",
                    "R N G M X G N R" // 'X' → invalid
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock(); // kvMap 정상화
            int boardStartIdx = checker.testFindBoardStartIndex(); // board 시작 인덱스 얻기
            checker.testCheckBoardLines(boardStartIdx); // boardLines 정상화
            checker.getErrors().clear(); // 에러 초기화 후 진짜 테스트
            boolean result = checker.testCheckPieceSymbols();

            System.out.println("Case 4 - Invalid board (gameType 3 - Chaturanga)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Invalid Chaturanga board should fail checkPieceSymbols.");
        }

        // Case 5 - Valid board (gameType 2 - ThreeCheck)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:2",
                    "castling:1",
                    "promotion:1",
                    "enpassant:0",
                    "ThreeCheckW:0",
                    "ThreeCheckB:1",
                    "board:",
                    "black",
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
            checker.testCheckKeyValueBlock(); // kvMap 정상화
            int boardStartIdx = checker.testFindBoardStartIndex(); // board 시작 인덱스 얻기
            checker.testCheckBoardLines(boardStartIdx); // boardLines 정상화
            checker.getErrors().clear(); // 에러 초기화 후 진짜 테스트
            boolean result = checker.testCheckPieceSymbols();

            System.out.println("Case 5 - Valid board (gameType 2 - ThreeCheck)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertTrue(result, "Valid ThreeCheck board should pass checkPieceSymbols.");
        }

        // Case 6 - Invalid board (gameType 2 - ThreeCheck)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:2",
                    "castling:1",
                    "promotion:1",
                    "enpassant:0",
                    "ThreeCheckW:0",
                    "ThreeCheckB:1",
                    "board:",
                    "white",
                    "r n b q k b n r",
                    "p p p p p p p p",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    "P P P P P P P P",
                    "R N B Q X B N R" // 'X' → invalid
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock(); // kvMap 정상화
            int boardStartIdx = checker.testFindBoardStartIndex(); // board 시작 인덱스 얻기
            checker.testCheckBoardLines(boardStartIdx); // boardLines 정상화
            checker.getErrors().clear(); // 에러 초기화 후 진짜 테스트
            boolean result = checker.testCheckPieceSymbols();

            System.out.println("Case 6 - Invalid board (gameType 2 - ThreeCheck)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Invalid ThreeCheck board should fail checkPieceSymbols.");
        }

        // Case 7 - Valid board (gameType 4 - PawnGame)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:4",
                    "castling:0",
                    "promotion:0",
                    "enpassant:0",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "black",
                    "p . . . k . p .",
                    ". . . p p . . p",
                    ". p . . . p . .",
                    ". . p . . . . .",
                    ". . P . P . . .",
                    "P . . P . . . .",
                    ". . . . . P . P",
                    ". P . . K . P ."
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock(); // kvMap 정상화
            int boardStartIdx = checker.testFindBoardStartIndex(); // board 시작 인덱스 얻기
            checker.testCheckBoardLines(boardStartIdx); // boardLines 정상화
            checker.getErrors().clear(); // 에러 초기화 후 진짜 테스트
            boolean result = checker.testCheckPieceSymbols();

            System.out.println("Case 7 - Valid board (gameType 4 - PawnGame)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertTrue(result, "Valid PawnGame board should pass checkPieceSymbols.");
        }

        // Case 8 - Invalid board (gameType 4 - PawnGame)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:4",
                    "castling:0",
                    "promotion:0",
                    "enpassant:0",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "r n b q k b n r",
                    "p p p p p p p p",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    "P P P P P P P P",
                    "R N B Q X B N R" // 'X' → invalid
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock(); // kvMap 정상화
            int boardStartIdx = checker.testFindBoardStartIndex(); // board 시작 인덱스 얻기
            checker.testCheckBoardLines(boardStartIdx); // boardLines 정상화
            checker.getErrors().clear(); // 에러 초기화 후 진짜 테스트
            boolean result = checker.testCheckPieceSymbols();

            System.out.println("Case 8 - Invalid board (gameType 4 - PawnGame)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Invalid PawnGame board should fail checkPieceSymbols.");
        }
    }

    @Test
    public void testCheckKingCount() {
        // Case 1 - Valid board (1 white king, 1 black king)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:1",
                    "castling:1",
                    "promotion:1",
                    "enpassant:0",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
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
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckKingCount();

            System.out.println("Case 1 - Valid board (1 white king, 1 black king)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertTrue(result, "Valid board should pass checkKingCount.");
        }

        // Case 2-1 - Invalid board (no white king)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:1",
                    "castling:1",
                    "promotion:1",
                    "enpassant:0",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "r n b q k b n r",
                    "p p p p p p p p",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    "P P P P P P P P",
                    "R N B Q . B N R" // K 없음
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckKingCount();

            System.out.println("Case 2-1 - Invalid board (no white king)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Invalid board (no white king) should fail checkKingCount.");
        }

        // Case 2-2 - Invalid board (2 white kings)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:1",
                    "castling:1",
                    "promotion:1",
                    "enpassant:0",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "r n b q k b n r",
                    "p p p p p p p p",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    "P P P P P P P K",
                    "R N B Q K B N R"
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckKingCount();

            System.out.println("Case 2-2 - Invalid board (2 white kings)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Invalid board (2 white kings) should fail checkKingCount.");
        }

        // Case 3-1 - Invalid board (no black king)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:1",
                    "castling:1",
                    "promotion:1",
                    "enpassant:0",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "r n b q . b n r",
                    "p p p p p p p p",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    "P P P P P P P P",
                    "R N B Q K B N R"
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckKingCount();

            System.out.println("Case 3-1 - Invalid board (no black king)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Invalid board (no black king) should fail checkKingCount.");
        }

        // Case 3-2 - Invalid board (2 black kings)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:1",
                    "castling:1",
                    "promotion:1",
                    "enpassant:0",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "r n b q k b n r", // 2 black kings
                    "p p . p p p p p",
                    ". . . k . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    "P P P P P P P P",
                    "R N B Q K B N R"
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckKingCount();

            System.out.println("Case 3-2 - Invalid board (2 black kings)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Invalid board (2 black kings) should fail checkKingCount.");
        }

        // Case 4 - Invalid board (no kings at all)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:1",
                    "castling:1",
                    "promotion:1",
                    "enpassant:0",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "r n b q . b n r", // black king 없음
                    "p p p p p p p p",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    "P P P P P P P P",
                    "R N B Q . B N R" // white king 없음
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckKingCount();

            System.out.println("Case 4 - Invalid board (no kings at all)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Invalid board (no kings at all) should fail checkKingCount.");
        }

        // Case 5 - Invalid board (2 white kings, 2 black kings)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:1",
                    "castling:1",
                    "promotion:1",
                    "enpassant:0",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "r n b q k k n r", // 2 black kings
                    "p p p p p p p p",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    "P P P P P P P P",
                    "R N B Q K K N R" // 2 white kings
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckKingCount();

            System.out.println("Case 5 - Invalid board (2 white kings, 2 black kings)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Invalid board (2 white kings, 2 black kings) should fail checkKingCount.");
        }
    }

    @Test
    public void testCheckRuleFlags() {
        // Case 1 - Valid rule flags (0 or 1 for all)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:1",
                    "castling:1",
                    "promotion:0",
                    "enpassant:1",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
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
            checker.testCheckKeyValueBlock();
            checker.getErrors().clear();
            boolean result = checker.testCheckRuleFlags();

            System.out.println("Case 1-1 - Valid rule flags (0 or 1 for all)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertTrue(result, "Valid rule flags should pass checkRuleFlags.");
        }

        // Case 2 - Invalid castling
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:1",
                    "castling:2", // Invalid
                    "promotion:0",
                    "enpassant:1",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
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
            checker.testCheckKeyValueBlock();
            checker.getErrors().clear();
            boolean result = checker.testCheckRuleFlags();

            System.out.println("Case 2 - Invalid castling");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Invalid castling should fail checkRuleFlags.");
        }

        // Case 3 - Invalid promotion
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:1",
                    "castling:1",
                    "promotion:x", // Invalid
                    "enpassant:1",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
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
            checker.testCheckKeyValueBlock();
            checker.getErrors().clear();
            boolean result = checker.testCheckRuleFlags();

            System.out.println("Case 3 - Invalid promotion");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Invalid promotion should fail checkRuleFlags.");
        }

        // Case 4 - Invalid enpassant
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:1",
                    "castling:1",
                    "promotion:0",
                    "enpassant:-1", // Invalid
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
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
            checker.testCheckKeyValueBlock();
            checker.getErrors().clear();
            boolean result = checker.testCheckRuleFlags();

            System.out.println("Case 4 - Invalid enpassant");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Invalid enpassant should fail checkRuleFlags.");
        }

        // Case 5 - All invalid (robustness test)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:1",
                    "castling:-156", // Invalid
                    "promotion:abc", // Invalid
                    "enpassant:9", // Invalid
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
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
            checker.testCheckKeyValueBlock();
            checker.getErrors().clear();
            boolean result = checker.testCheckRuleFlags();

            System.out.println("Case 5 - All invalid (robustness test)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "All invalid rule flags should fail checkRuleFlags.");
        }
    }

    @Test
    public void testCheckThreeCheckSettings() {
        // Case 1-1 - Valid ThreeCheck settings (game_type == 2, values 0~2)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:2",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:2",
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
            checker.testCheckKeyValueBlock();
            checker.getErrors().clear();
            boolean result = checker.testCheckThreeCheckSettings();

            System.out.println("Case 1-1 - Valid ThreeCheck settings (game_type == 2, values 0~2)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertTrue(result, "Valid ThreeCheck settings should pass checkThreeCheckSettings.");
        }

        // Case 1-2 - Valid ThreeCheck settings (game_type != 2, values == -1)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:1",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
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
            checker.testCheckKeyValueBlock();
            checker.getErrors().clear();
            boolean result = checker.testCheckThreeCheckSettings();

            System.out.println("Case 1-2 - Valid ThreeCheck settings (game_type != 2, values == -1)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertTrue(result, "Valid ThreeCheck settings (game_type != 2) should pass checkThreeCheckSettings.");
        }

        // Case 2-1 - Invalid: game_type == 2, ThreeCheckW == -1
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:2",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:-1", // Invalid
                    "ThreeCheckB:1",
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
            checker.testCheckKeyValueBlock();
            checker.getErrors().clear();
            boolean result = checker.testCheckThreeCheckSettings();

            System.out.println("Case 2-1 - Invalid: game_type == 2, ThreeCheckW == -1");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Invalid ThreeCheck settings (game_type==2, -1 value) should fail checkThreeCheckSettings.");
        }

        // Case 2-2 - Invalid: game_type == 2, ThreeCheckB == -1
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:2",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:0",
                    "ThreeCheckB:-1", // Invalid
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
            checker.testCheckKeyValueBlock();
            checker.getErrors().clear();
            boolean result = checker.testCheckThreeCheckSettings();

            System.out.println("Case 2-2 - Invalid: game_type == 2, ThreeCheckB == -1");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Invalid ThreeCheck settings (game_type == 2, -1 value) should fail checkThreeCheckSettings.");
        }

        // Case 3-1 - Invalid: game_type != 2, ThreeCheckW == 0~2
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:1",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:1", // Invalid
                    "ThreeCheckB:-1",
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
            checker.testCheckKeyValueBlock();
            checker.getErrors().clear();
            boolean result = checker.testCheckThreeCheckSettings();

            System.out.println("Case 3-1 - Invalid: game_type != 2, ThreeCheckW == 0~2");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Invalid ThreeCheck settings (game_type!=2, value 0~2) should fail checkThreeCheckSettings.");
        }

        // Case 3-2 - Invalid: game_type != 2, ThreeCheckB == 0~2
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:1",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:2", // Invalid
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
            checker.testCheckKeyValueBlock();
            checker.getErrors().clear();
            boolean result = checker.testCheckThreeCheckSettings();

            System.out.println("Case 3-2 - Invalid: game_type != 2, ThreeCheckB == 0~2");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Invalid ThreeCheck settings (game_type!=2, value 0~2) should fail checkThreeCheckSettings.");
        }

        // Case 4-1 - Invalid: game_type == 2, ThreeCheckW value out of range (ex: 3)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:2",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:3", // Invalid
                    "ThreeCheckB:2",
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
            checker.testCheckKeyValueBlock();
            checker.getErrors().clear();
            boolean result = checker.testCheckThreeCheckSettings();

            System.out.println("Case 4-1 - Invalid: game_type == 2, ThreeCheckW value out of range (ex: 3)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Invalid ThreeCheck settings (game_type==2, out of range value) should fail checkThreeCheckSettings.");
        }

        // Case 4-2 - Invalid: game_type==2, ThreeCheckW or ThreeCheckB value out of range (ex: -5)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:2",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:1",
                    "ThreeCheckB:-5", // Invalid
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
            checker.testCheckKeyValueBlock();
            checker.getErrors().clear();
            boolean result = checker.testCheckThreeCheckSettings();

            System.out.println("Case 4-2 - Invalid: game_type == 2, ThreeCheckB value out of range (ex: 3)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Invalid ThreeCheck settings (game_type==2, out of range value) should fail checkThreeCheckSettings.");
        }
    }

    @Test
    public void testCheckPieceCoordinates() {
        // Case 1 - Valid coordinates (정상 케이스, Pf/pf 포함)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:1",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "K 4 7",
                    "k 4 0",
                    "R 0 7",
                    "r 0 0",
                    "Pf 0 6", // board[6][0] == "P"
                    "pf 0 1", // board[1][0] == "p"
                    "r n b q k b n r",
                    "p . . . . . . .",
                    ". p p p p p p p",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". P P P P P P P",
                    "P . . . . . . .",
                    "R N B Q K B N R"
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckPieceCoordinates();

            System.out.println("Case 1 - Valid coordinates (정상 케이스, Pf/pf 포함)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertTrue(result, "Valid coordinates should pass checkPieceCoordinates.");
        }

        // Case 2-1 - 형식 오류 (표준 체스, tokens 개수 오류)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:1",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "K 4", // 형식 오류
                    "r 7 0 1", // 형식 오류
                    "Pf 0 6",
                    "pf 0 1",
                    "r n b q k b n r",
                    "p . . . . . . .",
                    ". p p p p p p p",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". P P P P P P P",
                    "P . . . . . . .",
                    "R N B Q K B N R"
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckPieceCoordinates();

            System.out.println("Case 2-1 - 형식 오류 (표준 체스, tokens 개수 오류)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Invalid format should fail checkPieceCoordinates.");
        }

        // Case 2-2 - 형식 오류 (쓰리 체크, game_type 2)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:2",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:0",
                    "ThreeCheckB:1",
                    "board:",
                    "white",
                    "K 4", // 형식 오류
                    "r 7 0 1", // 형식 오류
                    "Pf 0 6",
                    "pf 0 1",
                    "r n b q k b n r",
                    "p . . . . . . .",
                    ". p p p p p p p",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". P P P P P P P",
                    "P . . . . . . .",
                    "R N B Q K B N R"
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckPieceCoordinates();

            System.out.println("Case 2-2 - 형식 오류 (쓰리 체크, game_type 2)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Invalid format should fail checkPieceCoordinates for game_type 2.");
        }

        // Case 2-3 - 형식 오류 (차투랑가, game_type 3)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:3",
                    "castling:0",
                    "promotion:0",
                    "enpassant:0",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "K 4", // 형식 오류
                    "r 7 0 1", // 형식 오류
                    "r n g m k g n r",
                    "f . . . . . . .",
                    ". f f f f f f f",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". F F F F F F F",
                    "F . . . . . . .",
                    "R N G M K G N R"
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckPieceCoordinates();

            System.out.println("Case 2-3 - 형식 오류 (차투랑가, game_type 3)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Invalid format should fail checkPieceCoordinates for game_type 3.");
        }

        // Case 2-4 - 형식 오류 (폰게임, game_type 4)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:4",
                    "castling:0",
                    "promotion:0",
                    "enpassant:0",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "K 4", // 형식 오류
                    "r 7 0 1", // 형식 오류
                    "p . . . k . p .",
                    ". . . p p . . p",
                    ". p . . . p . .",
                    ". . p . . . . .",
                    ". . P . P . . .",
                    "P . . P . . . .",
                    ". . . . . P . P",
                    ". P . . K . P ."
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckPieceCoordinates();

            System.out.println("Case 2-4 - 형식 오류 (폰게임, game_type 4)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Invalid format should fail checkPieceCoordinates for game_type 4.");
        }


        // Case 3-1 - 잘못된 기물 기호 (표준 체스, game_type 1)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:1",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "X 4 7", // Invalid 기물 기호
                    "Pf 0 6",
                    "pf 0 1",
                    "r n b q k b n r",
                    "p . . . . . . .",
                    ". p p p p p p p",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". P P P P P P P",
                    "P . . . . . . .",
                    "R N B Q K B N R"
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckPieceCoordinates();

            System.out.println("Case 3-1 - 잘못된 기물 기호 (표준 체스, game_type 1)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Invalid piece symbol should fail checkPieceCoordinates.");
        }

        // Case 3-2 - 잘못된 기물 기호 (쓰리체크, game_type 2)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:2",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:1",
                    "ThreeCheckB:2",
                    "board:",
                    "white",
                    "# 4 7", // Invalid 기물 기호
                    "Pf 0 6",
                    "pf 0 1",
                    "r n b q k b n r",
                    "p . . . . . . .",
                    ". p p p p p p p",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". P P P P P P P",
                    "P . . . . . . .",
                    "R N B Q K B N R"
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckPieceCoordinates();

            System.out.println("Case 3-2 - 잘못된 기물 기호 (쓰리체크, game_type 2)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Invalid piece symbol should fail checkPieceCoordinates for game_type 2.");
        }

        // Case 3-3 - 잘못된 기물 기호 (차투랑가, game_type 3)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:4",
                    "castling:0",
                    "promotion:0",
                    "enpassant:0",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "5 4 7", // Invalid 기물 기호
                    "r n g m k g n r",
                    "f . . . . . . .",
                    ". f f f f f f f",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". F F F F F F F",
                    "F . . . . . . .",
                    "R N G M K G N R"
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckPieceCoordinates();

            System.out.println("Case 3-3 - 잘못된 기물 기호 (차투랑가, game_type 3)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Invalid piece symbol should fail checkPieceCoordinates for game_type 4.");
        }

        // Case 3-4 - 잘못된 기물 기호 (폰게임, game_type 4)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:4",
                    "castling:0",
                    "promotion:0",
                    "enpassant:0",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "m 4 7", // Invalid 기물 기호
                    "p . . . k . p .",
                    ". . . p p . . p",
                    ". p . . . p . .",
                    ". . p . . . . .",
                    ". . P . P . . .",
                    "P . . P . . . .",
                    ". . . . . P . P",
                    ". P . . K . P ."
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckPieceCoordinates();

            System.out.println("Case 3-4 - 잘못된 기물 기호 (폰게임, game_type 4)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Invalid piece symbol should fail checkPieceCoordinates for game_type 4.");
        }


        // Case 4-1 - 좌표 범위 오류
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:1",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "K 8 5", // col 범위 오류
                    "k 5 -1", // row 범위 오류
                    "Pf 0 6",
                    "pf 0 1",
                    "r n b q k b n r",
                    "p . . . . . . .",
                    ". p p p p p p p",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". P P P P P P P",
                    "P . . . . . . .",
                    "R N B Q K B N R"
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckPieceCoordinates();

            System.out.println("Case 4-1 - 좌표 범위 오류");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Coordinate out of bounds should fail checkPieceCoordinates.");
        }

        // Case 4-2 - 좌표 범위 오류 (쓰리체크, game_type 2)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:2",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:1",
                    "ThreeCheckB:2",
                    "board:",
                    "white",
                    "K 4 8", // row 범위 오류
                    "k 5 9", // row 범위 오류
                    "Pf 0 6",
                    "pf 0 1",
                    "r n b q k b n r",
                    "p . . . . . . .",
                    ". p p p p p p p",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". P P P P P P P",
                    "P . . . . . . .",
                    "R N B Q K B N R"
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckPieceCoordinates();

            System.out.println("Case 4-2 - 좌표 범위 오류 (쓰리체크, game_type 2)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Coordinate out of bounds should fail checkPieceCoordinates for game_type 2.");
        }

        // Case 4-3 - 좌표 범위 오류 (차투랑가, game_type 3)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:3",
                    "castling:0",
                    "promotion:0",
                    "enpassant:0",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "K -1 5", // col 범위 오류
                    "k -10 -1", // row 범위 오류
                    "r n g m k g n r",
                    "f . . . . . . .",
                    ". f f f f f f f",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". F F F F F F F",
                    "F . . . . . . .",
                    "R N G M K G N R"
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckPieceCoordinates();

            System.out.println("Case 4-3 - 좌표 범위 오류 (차투랑가, game_type 3)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Coordinate out of bounds should fail checkPieceCoordinates for game_type 3.");
        }

        // Case 4-4 - 좌표 범위 오류 (폰게임, game_type 4)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:4",
                    "castling:0",
                    "promotion:0",
                    "enpassant:0",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "K 4 -1", // row 범위 오류
                    "k 8 -3", // row 범위 오류
                    "Pf 0 6",
                    "pf 0 1",
                    ". . . . k . p .",
                    "p . . p p . . p",
                    ". p . . . p . .",
                    ". . p . . . . .",
                    ". . P . P . . .",
                    ". . . P . . . .",
                    "P . . . . P . P",
                    ". P . . K . P ."
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckPieceCoordinates();

            System.out.println("Case 4-4 - 좌표 범위 오류 (폰게임, game_type 4)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Coordinate out of bounds should fail checkPieceCoordinates for game_type 4.");
        }

        // Case 5-1 - 빈 칸 좌표 오류 (표준 체스, game_type 1)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:1",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "K 3 3", // 빈 칸 좌표
                    "Pf 0 6",
                    "pf 0 1",
                    "r n b q k b n r",
                    "p . . . . . . .",
                    ". p p p p p p p",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". P P P P P P P",
                    "P . . . . . . .",
                    "R N B Q K B N R"
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckPieceCoordinates();

            System.out.println("Case 5-1 - 빈 칸 좌표 오류 (표준 체스, game_type 1)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Empty cell coordinate should fail checkPieceCoordinates.");
        }

        // Case 5-2 - 빈 칸 좌표 오류 (쓰리체크, game_type 2)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:2",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:1",
                    "ThreeCheckB:2",
                    "board:",
                    "white",
                    "K 4 4", // 빈 칸 좌표 (. 위치)
                    "Pf 0 6",
                    "pf 0 1",
                    "r n b q k b n r",
                    "p . . . . . . .",
                    ". p p p p p p p",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". P P P P P P P",
                    "P . . . . . . .",
                    "R N B Q K B N R"
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckPieceCoordinates();

            System.out.println("Case 5-2 - 빈 칸 좌표 오류 (쓰리체크, game_type 2)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Empty cell coordinate should fail checkPieceCoordinates for game_type 2.");
        }

        // Case 5-3 - 빈 칸 좌표 오류 (차투랑가, game_type 3)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:3",
                    "castling:0",
                    "promotion:0",
                    "enpassant:0",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "K 3 1", // 빈 칸 좌표 (. 위치)
                    // Pf/pf 제거
                    "r n g m k g n r",
                    "f . . . . . . .",
                    ". f f f f f f f",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". F F F F F F F",
                    "F . . . . . . .",
                    "R N G M K G N R"
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckPieceCoordinates();

            System.out.println("Case 5-3 - 빈 칸 좌표 오류 (차투랑가, game_type 3)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Empty cell coordinate should fail checkPieceCoordinates for game_type 3.");
        }

        // Case 5-4 - 빈 칸 좌표 오류 (폰게임, game_type 4)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:4",
                    "castling:0",
                    "promotion:0",
                    "enpassant:0",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "K 3 3", // 빈 칸 좌표 (. 위치)
                    "Pf 0 6",
                    "pf 0 1",
                    ". . . . k . p .",
                    "p . . p p . . p",
                    ". p . . . p . .",
                    ". . p . . . . .",
                    ". . P . P . . .",
                    ". . . P . . . .",
                    "P . . . . P . P",
                    ". P . . K . P ."
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckPieceCoordinates();

            System.out.println("Case 5-4 - 빈 칸 좌표 오류 (폰게임, game_type 4)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Empty cell coordinate should fail checkPieceCoordinates for game_type 4.");
        }

        // Case 6-1 - 기물 불일치 오류 (표준 체스, game_type 1)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:1",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "R 0 0", // board에는 'r' 있음
                    "R 4 0", // board에는 'k' 있음
                    "Pf 0 6",
                    "pf 0 1",
                    "r n b q k b n r",
                    "p . . . . . . .",
                    ". p p p p p p p",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". P P P P P P P",
                    "P . . . . . . .",
                    "R N B Q K B N R"
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckPieceCoordinates();

            System.out.println("Case 6-1 - 기물 불일치 오류 (표준 체스, game_type 1)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Piece mismatch should fail checkPieceCoordinates.");
        }

        // Case 6-2 - 기물 불일치 오류 (쓰리체크, game_type 2)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:2",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:1",
                    "ThreeCheckB:2",
                    "board:",
                    "white",
                    "R 0 0", // board에는 'r' 있음 → 불일치
                    "Pf 0 6",
                    "pf 0 1",
                    "r n b q k b n r",
                    "p . . . . . . .",
                    ". p p p p p p p",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". P P P P P P P",
                    "P . . . . . . .",
                    "R N B Q K B N R"
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckPieceCoordinates();

            System.out.println("Case 6-2 - 기물 불일치 오류 (쓰리체크, game_type 2)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Piece mismatch should fail checkPieceCoordinates for game_type 2.");
        }

        // Case 6-3 - 기물 불일치 오류 (차투랑가, game_type 3)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:3",
                    "castling:0",
                    "promotion:0",
                    "enpassant:0",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "R 0 0", // board에는 'r' 있음 → 불일치
                    "Pf 0 6",
                    "pf 0 1",
                    "r n g m k g n r",
                    "f . . . . . . .",
                    ". f f f f f f f",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". F F F F F F F",
                    "F . . . . . . .",
                    "R N G M K G N R"
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckPieceCoordinates();

            System.out.println("Case 6-3 - 기물 불일치 오류 (차투랑가, game_type 3)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Piece mismatch should fail checkPieceCoordinates for game_type 3.");
        }

        // Case 6-4 - 기물 불일치 오류 (폰게임, game_type 4)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:4",
                    "castling:0",
                    "promotion:0",
                    "enpassant:0",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "R 0 0", // board에는 'p' 또는 빈칸 예상 → 'R' 불일치
                    "Pf 0 6",
                    "pf 0 1",
                    "p p p p p p p p",
                    "p . . . . . . .",
                    ". p p p p p p p",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". P P P P P P P",
                    "P . . . . . . .",
                    "P P P P P P P P"
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            checker.testCheckKeyValueBlock();
            int boardStartIdx = checker.testFindBoardStartIndex();
            checker.testCheckBoardLines(boardStartIdx);
            checker.getErrors().clear();
            boolean result = checker.testCheckPieceCoordinates();

            System.out.println("Case 6-4 - 기물 불일치 오류 (폰게임, game_type 4)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertFalse(result, "Piece mismatch should fail checkPieceCoordinates for game_type 4.");
        }
    }

    @Test
    public void testCheckGameEnd() {
        // Case 1 - 정상 진행 중 (표준 체스, game_type 1)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:1",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "K 4 7",
                    "k 4 0",
                    "Pf 0 6",
                    "pf 0 1",
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

            System.out.println("Case 1 - 정상 진행 중 (표준 체스, game_type 1)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertNotNull(board, "Normal board should pass checkGameEnd for game_type 1.");
        }

        // Case 2-1 - CheckMate 상태 (표준 체스, game_type 1)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:1",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "black",
                    "K 6 7",
                    "k 6 0",
                    "Q . . . . . k .",
                    ". . . . . p p p",
                    ". . . . . . Q .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . R .",
                    ". . . . . . K ."
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            Board board = checker.validateFile();

            System.out.println("Case 2-1 - CheckMate 상태 (표준 체스, game_type 1)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertNull(board, "CheckMate board should fail checkGameEnd for game_type 1.");
        }

        // Case 2-2 - CheckMate 상태 (쓰리체크, game_type 2)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:2",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:0",
                    "ThreeCheckB:0",
                    "board:",
                    "black",
                    "K 6 7",
                    "k 6 0",
                    "Q . . . . . k .",
                    ". . . . . p p p",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . R .",
                    ". . . . . . K ."
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            Board board = checker.validateFile();

            System.out.println("Case 2-2 - CheckMate 상태 (쓰리체크, game_type 2)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertNull(board, "CheckMate board should fail checkGameEnd for game_type 1.");
        }

        // Case 2-3 - CheckMate 상태 (차투랑가, game_type 3)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:3",
                    "castling:0",
                    "promotion:0",
                    "enpassant:1",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "black",
                    "K 3 6",
                    "k 6 0",
                    "R . . . . . k .",
                    ". . . . . f f f",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . G . . .",
                    ". . . K . . . .",
                    ". . . . . . . ."
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            Board board = checker.validateFile();

            System.out.println("Case 2-3 - CheckMate 상태 (차투랑가, game_type 3)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertNull(board, "CheckMate board should fail checkGameEnd for game_type 1.");
        }

        // Case 2-4 - CheckMate 상태 (폰게임, game_type 4)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:4",
                    "castling:0",
                    "promotion:1",
                    "enpassant:0",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "black",
                    "K 3 6",
                    "k 6 0",
                    "R . . . . . k .",
                    ". . . . . p p p",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . Q . . .",
                    ". . . K . . . .",
                    ". . . . . . . ."
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            Board board = checker.validateFile();

            System.out.println("Case 2-4 - CheckMate 상태 (폰게임, game_type 4)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertNull(board, "CheckMate board should fail checkGameEnd for game_type 1.");
        }

        // Case 3-1 - StaleMate 상태 (표준 체스, game_type 1)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:1",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "black",
                    "K 6 7",
                    "k 7 0",
                    ". . . . . . . k",
                    ". . . . . . R .",
                    ". . . . . . Q .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . K ."
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            Board board = checker.validateFile();

            System.out.println("Case 3-1 - StaleMate 상태 (표준 체스, game_type 1)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertNull(board, "StaleMate board should fail checkGameEnd for game_type 1.");
        }

        // Case 3-2 - StaleMate 상태 (쓰리체크, game_type 2)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:2",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:0",
                    "ThreeCheckB:0",
                    "board:",
                    "black",
                    "K 6 7",
                    "k 7 0",
                    ". . . . . . . k",
                    ". . . . . . R .",
                    ". . . . . . Q .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . K ."
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            Board board = checker.validateFile();

            System.out.println("Case 3-2 - StaleMate 상태 (쓰리체크, game_type 2)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertNull(board, "StaleMate board should fail checkGameEnd for game_type 1.");
        }

        // Case 3-3 - StaleMate 상태 (차투랑가, game_type 3)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:3",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "black",
                    "K 5 1",
                    "k 7 0",
                    ". . . . . . . k",
                    ". . . . . K . .",
                    ". . . . . . . .",
                    ". . . . . G . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . ."
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            Board board = checker.validateFile();

            System.out.println("Case 3-3 - StaleMate 상태 (차투랑가, game_type 3)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertNull(board, "StaleMate board should fail checkGameEnd for game_type 1.");
        }

        // Case 3-4 - StaleMate 상태 (폰게임, game_type 4)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:4",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "black",
                    "K 6 7",
                    "k 7 0",
                    ". . . . . . . k",
                    ". . . . . . R .",
                    ". . . . . . Q .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . . K ."
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            Board board = checker.validateFile();

            System.out.println("Case 3-4 - StaleMate 상태 (폰게임, game_type 4)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertNull(board, "StaleMate board should fail checkGameEnd for game_type 1.");
        }

        // Case 4-1 - InsufficientPieces 상태 (표준 체스, game_type 1)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:1",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "K 5 2",
                    "k 3 5",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . K . .",
                    ". . . . . . . .",
                    ". . . . . . N .",
                    ". . . k . . . .",
                    ". . . . . . . .",
                    ". . . . . . . ."
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            Board board = checker.validateFile();

            System.out.println("Case 4-1 - InsufficientPieces 상태 (표준 체스, game_type 1)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertNull(board, "InsufficientPieces board should fail checkGameEnd for game_type 1.");
        }

        // Case 4-2 - InsufficientPieces 상태 (쓰리체크, game_type 2)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:2",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:0",
                    "ThreeCheckB:0",
                    "board:",
                    "black",
                    "K 3 5",
                    "k 5 2",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . k . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . K . . . .",
                    ". . . . . . . .",
                    ". . . . . . . ."
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            Board board = checker.validateFile();

            System.out.println("Case 4-2 - InsufficientPieces 상태 (쓰리체크, game_type 2)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertNull(board, "InsufficientPieces board should fail checkGameEnd for game_type 1.");
        }

        // Case 4-3 - InsufficientPieces 상태 (차투랑가, game_type 3)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:3",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "black",
                    "K 3 5",
                    "k 5 2",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . k . .",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . K . . . .",
                    ". . . . . . . .",
                    ". . . . . . . ."
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            Board board = checker.validateFile();

            System.out.println("Case 4-3 - InsufficientPieces 상태 (차투랑가, game_type 3)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertNull(board, "InsufficientPieces board should fail checkGameEnd for game_type 1.");
        }

        // Case 4-4 - InsufficientPieces 상태 (폰게임, game_type 1)
        {
            List<String> lines = List.of(
                    "id:test123",
                    "save_name:save123456",
                    "game_type:4",
                    "castling:1",
                    "promotion:1",
                    "enpassant:1",
                    "ThreeCheckW:-1",
                    "ThreeCheckB:-1",
                    "board:",
                    "white",
                    "K 5 2",
                    "k 3 5",
                    ". . . . . . . .",
                    ". . . . . . . .",
                    ". . . . . K . .",
                    ". . . . . . . .",
                    ". . . . . . N .",
                    ". . . k . . . .",
                    ". . . . . . . .",
                    ". . . . . . . ."
            );

            SaveIntegrityChecker checker = new SaveIntegrityChecker(lines);
            Board board = checker.validateFile();

            System.out.println("Case 4-4 - InsufficientPieces 상태 (폰게임, game_type 4)");
            for (String error : checker.getErrors()) {
                System.out.println("    " + error);
            }
            System.out.println();

            assertNull(board, "InsufficientPieces board should fail checkGameEnd for game_type 1.");
        }
    }
}
