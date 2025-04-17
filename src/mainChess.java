import board.Board;
import board.Cell;
import data.PieceColor;

import java.util.Scanner;

public class mainChess {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Board board;
        PieceColor currentTurn;

        // 세이브 파일 로드 여부 결정 (파일 입출력은 외부 처리)
        System.out.print("세이브 파일을 불러오시겠습니까? (y/n): ");
        String loadAnswer = scan.nextLine().trim();
        if (loadAnswer.equalsIgnoreCase("y")) {
            // 예시 세이브 파일 데이터:
            // 첫 줄: 현재 턴, 이후 8줄: 보드 상태 (기호: r, n, b, q, k, b, n, r, p, ., etc.)
            currentTurn = PieceColor.BLACK;  // 예: 저장된 파일에 현재 턴이 BLACK으로 기록됨.
            String[] savedBoard = {
                    "r n b q k b n r",
                    "p p p p . p p p",
                    ". . . . . . . .",
                    ". . . . p . . .",
                    ". . . . P . . .",
                    ". . . . . . . .",
                    "P P P P . P P P",
                    "R N B Q K B N R"
            };
            board = new Board(true);
            System.out.println("세이브 파일 불러오기 완료!");
        } else {
            // 새 게임 시작
            board = new Board();
            currentTurn = PieceColor.WHITE;
        }

        // 초기 상태 출력
        System.out.println("[현재 체스판 상태]");
        System.out.println(board);
        System.out.println("현재 턴: " + currentTurn);

        // 게임 루프
        while (true) {
            System.out.print("이동할 기물의 위치와 도착 위치를 입력하세요 (예: e2 e4): ");
            String input = scan.nextLine();
            String[] tokens = input.split("\\s+");

            if (tokens.length != 2) {
                System.out.println("❌ 입력은 시작 위치와 도착 위치, 두 개여야 합니다. 예: e2 e4\n");
                continue;
            }

            String startNotation = tokens[0];
            String endNotation = tokens[1];

            if (!isValidNotation(startNotation) || !isValidNotation(endNotation)) {
                System.out.println("❌ 좌표 형식이 잘못되었습니다. 예: e2 e4\n");
                continue;
            }

            int[] startRC = Board.notationToCoordinate(startNotation);
            int[] endRC = Board.notationToCoordinate(endNotation);
            Cell startCell = board.getCell(startRC[0], startRC[1]);

            if (startCell.getPiece() == null) {
                System.out.println("❌ 선택한 칸에 기물이 없습니다.\n");
                continue;
            }

            if (startCell.getPiece().getColor() != currentTurn) {
                System.out.println("❌ 현재 턴의 기물이 아닙니다. (" + currentTurn + " 턴인데 다른 색 기물 선택)\n");
                continue;
            }

            boolean result = board.movePiece(startRC[0], startRC[1], endRC[0], endRC[1]);
            if (result) {
                System.out.println("✅ 이동 성공!");
                System.out.println(board);
                // 턴 전환
                currentTurn = (currentTurn == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
                System.out.println("턴이 전환되었습니다. 현재 턴: " + currentTurn + "\n");
            }


        }
        // scan.close(); // 무한 루프로 종료되지 않으므로 닫지 않습니다.
    }

    /**
     * 체스 좌표 표기법 (예: e2, a5 등)이 올바른지 검증합니다.
     */
    static boolean isValidNotation(String input) {
        if (input == null) return false;
        input = input.trim();
        if (input.isEmpty()) return false;
        if (input.length() != 2) return false;
        input = input.toLowerCase();
        char file = input.charAt(0);
        if (file < 'a' || file > 'h') return false;
        char rank = input.charAt(1);
        if (rank < '1' || rank > '8') return false;
        return true;
    }
}