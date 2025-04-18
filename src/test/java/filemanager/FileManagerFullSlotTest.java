package test.java.filemanager;

import board.Board;
import data.PieceColor;

import fileManager.FileManager;
import org.junit.jupiter.api.Test;
import piece.Bishop;
import piece.King;
import piece.Pawn;
import piece.Rook;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class FileManagerFullSlotTest {

    @Test
    public void testFullSlotSaveLoadAndPrint() {
        FileManager fileManager = FileManager.getInstance();

        // 1. 모든 슬롯에 기본 보드 저장
        for (int slot = 1; slot <= 5; slot++) {
            Board dummyBoard = new Board(true); // 빈 보드
            dummyBoard.setPieceTest(0, slot - 1, new Pawn(PieceColor.WHITE)); // 슬롯마다 다르게 한 칸만 기물 배치
            fileManager.overWriteSavedFile(slot, dummyBoard);
        }

        // 2. 랜덤 슬롯 하나 선택
        int randomSlot = new Random().nextInt(5) + 1;

        // 3. 랜덤 슬롯에 제대로된 기보 저장할 보드 만들기
        Board original = new Board(true);
        original.setPieceTest(0, 0, new Rook(PieceColor.BLACK));
        original.setPieceTest(0, 7, new Rook(PieceColor.BLACK));
        original.setPieceTest(7, 0, new Rook(PieceColor.WHITE));
        original.setPieceTest(7, 7, new Rook(PieceColor.WHITE));
        original.setPieceTest(4, 4, new King(PieceColor.WHITE));
        original.setPieceTest(3, 3, new Bishop(PieceColor.BLACK));
        original.turnChange(); // BLACK 턴으로 변경

        boolean saved = fileManager.overWriteSavedFile(randomSlot, original);
        assertTrue(saved);

        // 4. 새 보드에 로드
        Board loaded = new Board(true);
        boolean loadedSuccess = fileManager.loadSavedFile(randomSlot, loaded);
        assertTrue(loadedSuccess);

        // 5. 일치 여부 확인
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                var origPiece = original.getPieceAt(row, col);
                var loadedPiece = loaded.getPieceAt(row, col);

                if (origPiece == null || loadedPiece == null) {
                    assertEquals(origPiece, loadedPiece, "Mismatch at (" + row + "," + col + ")");
                } else {
                    assertEquals(origPiece.getClass(), loadedPiece.getClass(), "Class mismatch at (" + row + "," + col + ")");
                    assertEquals(origPiece.getColor(), loadedPiece.getColor(), "Color mismatch at (" + row + "," + col + ")");
                }
            }
        }

        assertEquals(original.getCurrentTurn(), loaded.getCurrentTurn(), "Turn mismatch");

        // 6. 해당 슬롯 출력
        System.out.println("=== 출력: 슬롯 " + randomSlot + " ===");
        printBoard(original);
        printBoard(loaded);


    // 8. 덮어쓰기 테스트
    System.out.println("\n=== 덮어쓰기 테스트 ===");

    // 첫 번째 보드 작성 및 저장
    Board overwriteBoard1 = new Board(true);
    overwriteBoard1.setPieceTest(0,0,new

    King(PieceColor.WHITE));
    overwriteBoard1.setPieceTest(1,1,new

    Pawn(PieceColor.WHITE));
    overwriteBoard1.setPieceTest(2,2,new

    Pawn(PieceColor.BLACK));
    overwriteBoard1.turnChange(); // BLACK 턴
    fileManager.overWriteSavedFile(1,overwriteBoard1);

    // 로드 후 출력
    Board loaded1 = new Board(true);
    fileManager.loadSavedFile(1,loaded1);
    System.out.println("▶ 첫 번째 저장 후 로드:");

    printBoard(loaded1);

    // 두 번째 보드 작성 및 덮어쓰기
    Board overwriteBoard2 = new Board(true);
    overwriteBoard2.setPieceTest(0,7,new

    King(PieceColor.BLACK));
    overwriteBoard2.setPieceTest(3,3,new

    Rook(PieceColor.BLACK));
    overwriteBoard2.setPieceTest(4,4,new

    Rook(PieceColor.WHITE));
    // 턴 그대로 WHITE
    fileManager.overWriteSavedFile(1,overwriteBoard2);

    // 로드 후 출력
    Board loaded2 = new Board(true);
    fileManager.loadSavedFile(1,loaded2);
    System.out.println("▶ 두 번째 덮어쓰기 후 로드:");

    printBoard(loaded2);
}


    private void printBoard(Board board) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                var piece = board.getPieceAt(row, col);
                System.out.print((piece == null ? "." : piece.getSymbol()) + " ");
            }
            System.out.println();
        }
        System.out.println("턴: " + board.getCurrentTurn());
    }
}
