import board.Board;
import board.Cell;
import board.PawnGameBoard;
import data.MoveResult;
import data.PieceColor;
import piece.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TestMain 클래스는 체스 보드 및 말 동작에 대한 테스트 케이스를 실행합니다.
 */
public class TestMain {
    public static void main(String[] args) {
        System.out.println("=== [1] useDefault = true (일반 체스판) ===");
        PawnGameBoard defaultBoard = new PawnGameBoard(true);
        System.out.println(defaultBoard.toString());

        System.out.println("=== [2] useDefault = false (폰 게임 보드) ===");
        PawnGameBoard pawnGameBoard = new PawnGameBoard(false);
        System.out.println(pawnGameBoard.toString());
    }



}
