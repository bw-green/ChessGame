import board.Board;

public class mainChess {
    public static void main(String[] args) {
        Board board = new Board();
        System.out.println("[초기 체스판 상태]");
        System.out.println(board);

        // 예시: 체스 표준 표기 "e2" -> "e4" 이동 시도
        String startNotation = "e2";
        String endNotation   = "e4";

        // 1) 표준 표기를 내부 좌표로 변환
        int[] startRC = Board.notationToCoordinate(startNotation); // e2 -> [6,4]
        int[] endRC   = Board.notationToCoordinate(endNotation);   // e4 -> [4,4]

        // 2) 변환된 좌표를 이용해 이동 수행
        boolean success = board.movePiece(startRC[0], startRC[1], endRC[0], endRC[1]);
        System.out.println("[이동 시도] " + startNotation + " -> " + endNotation);
        System.out.println("이동 성공 여부: " + success);

        // 이동 후 체스판 출력
        System.out.println("[이동 후 체스판 상태]");
        System.out.println(board);

        // 다른 예시: 백 나이트 (b1 -> c3) : "b1" -> "c3"
        // 내부 좌표: b1 -> [7,1], c3 -> [5,2]
        int[] startKnight = Board.notationToCoordinate("b1"); // [7,1]
        int[] endKnight   = Board.notationToCoordinate("c3"); // [5,2]
        success = board.movePiece(startKnight[0], startKnight[1], endKnight[0], endKnight[1]);
        System.out.println("[이동 시도] b1 -> c3");
        System.out.println("이동 성공 여부: " + success);
        System.out.println("[이동 후 체스판 상태]");
        System.out.println(board);
    }
}