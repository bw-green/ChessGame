import Input.GameInput;
import board.Board;
import data.GameInputReturn;
import Input.UserInput;
import data.MoveErrorType;
import data.MoveResult;
import data.PieceColor;

import java.util.Scanner;

public class TestMain {

    public static void main(String[] args) {

        Board board = new Board();
        while(true) {
            System.out.println(board);
            if(GameInput.gameInput()== GameInputReturn.COORDINATE_TRUE.getCode()){
                board.movePiece(UserInput.getFromRow(),UserInput.getFromCol(),UserInput.getToRow(),UserInput.getToCol());

            };

        }

    }
//    Scanner scan = new Scanner(System.in);
//    Board board;
//    PieceColor currentTurn;
//
//    // 세이브 파일 로드 여부 결정 (파일 입출력은 외부 처리)
//        System.out.print("세이브 파일을 불러오시겠습니까? (y/n): ");
//    String loadAnswer = scan.nextLine().trim();
//        if (loadAnswer.equalsIgnoreCase("y")) {
//        // 예시 세이브 파일 데이터:
//        // 첫 줄: 현재 턴, 이후 8줄: 보드 상태 (기호: r, n, b, q, k, b, n, r, p, ., etc.)
//        currentTurn = PieceColor.BLACK;  // 예: 저장된 파일에 현재 턴이 BLACK으로 기록됨.
//        String[] savedBoard = {
//                "r n b q k b n r",
//                "p p p p . p p p",
//                ". . . . . . . .",
//                ". . . . p . . .",
//                ". . . . P . . .",
//                ". . . . . . . .",
//                "P P P P . P P P",
//                "R N B Q K B N R"
//        };
//        board = new Board(true);
//        System.out.println("세이브 파일 불러오기 완료!");
//    } else {
//        // 새 게임 시작
//        board = new Board();
//        currentTurn = PieceColor.WHITE;
//    }
//
//    // 초기 상태 출력
//        System.out.println(board);
//
//    // 게임 루프
//        while (true) {
//        System.out.println("이동할 기물의 위치와 도착 위치를 입력하세요 (예: e2 e4): ");
//        System.out.print(currentTurn + " > ");
//        String input = scan.nextLine().trim();
//
//        // 1. 입력 비었는지 확인
//        if (input == null || input.isBlank()) {
//            printError(MoveErrorType.EMPTY_OR_NULL); // ← 문법 오류지만 예외적으로 같이 씀
//            continue;
//        }
//
//        // 2. 좌표 개수 확인
//        String[] tokens = input.split("\\s+");
//        if (tokens.length != 2) {
//            printError(MoveErrorType.COORDINATE_COUNT); // ← 역시 문법 오류 범주
//            continue;
//        }
//
//        String startNotation = tokens[0];
//        String endNotation = tokens[1];
//
//        // 3. 좌표 형식 검사
//        if (!isValidNotation(startNotation) || !isValidNotation(endNotation)) {
//            printError(MoveErrorType.INVALID_CHARACTER);
//            continue;
//        }
//
//        // 4. 의미 오류 검사 (보드 상태 기반)
//        MoveErrorType error = board.validateMoveMeaning(startNotation, endNotation, currentTurn);
//        if (error != null) {
//            printError(error);
//            continue;
//        }
//
//        // 5. 의미 오류 없음 → 이동 수행
//        int[] startRC = Board.notationToCoordinate(startNotation);
//        int[] endRC = Board.notationToCoordinate(endNotation);
//        MoveResult result = board.movePiece(startRC[0], startRC[1], endRC[0], endRC[1]);
//
//        if (result== MoveResult.SUCCESS) {
//            System.out.println(board);
//            currentTurn = (currentTurn == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
//        } else {
//            // (보통 여기 안 와야 함)
//            printError(MoveErrorType.INVALID_MOVE_FOR_THIS_PIECE);
//        }
//    }
//    // scan.close(); // 무한 루프로 종료되지 않으므로 닫지 않습니다.
//}
//
///**
// * 체스 좌표 표기법 (예: e2, a5 등)이 올바른지 검증합니다.
// */
//static boolean isValidNotation(String input) {
//    if (input == null) return false;
//    input = input.trim();
//    if (input.isEmpty()) return false;
//    if (input.length() != 2) return false;
//    input = input.toLowerCase();
//    char file = input.charAt(0);
//    if (file < 'a' || file > 'h') return false;
//    char rank = input.charAt(1);
//    if (rank < '1' || rank > '8') return false;
//    return true;
//}
//
//static void printError(MoveErrorType error) {
//    System.out.println("==================================================");
//    System.out.println(error.getMessage());
//    System.out.println("==================================================");
//    System.out.println();
//}
}
