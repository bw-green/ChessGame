import board.Board;
import board.Cell;
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
        // Test 0: 기본 보드판 전체 출력
        Board b0 = new Board();
        System.out.println("=== Entire toString ===");
        System.out.println(b0);

        List<TestCase> tests = new ArrayList<>();

        // Test 1: 기본 생성자 호출 후 초기화 검증
        Board b1 = new Board();
        tests.add(new TestCase("1.1", "getCell", "0,0", "r"));
        tests.get(tests.size() - 1).actual = b1.getCell(0, 0).toString();
        tests.add(new TestCase("1.2", "getCell", "7,4", "K"));
        tests.get(tests.size() - 1).actual = b1.getCell(7, 4).toString();

        // Test 2: 빈 보드 생성 검증
        Board b2 = new Board(true);
        tests.add(new TestCase("2.1", "getCell", "0,0", "."));
        tests.get(tests.size() - 1).actual = b2.getCell(0, 0).toString();

        // Test 3: getCell null 범위 검사
        addNullCellTests(tests, b1);

        // Test 4: isPathClear 추가 테스트 케이스
        addIsPathClearTests(tests);

        // Test 5: 기본 초기화 보드에서 isPathClear 검증
        addDefaultBoardPathTests(tests);

        // Test 6: movePiece 통합 테스트 (각 기물별)
        // Pawn
        Board bPawn = new Board();
        tests.add(new TestCase("6.1", "movePiece(Pawn)", "6,4->5,4", "SUCCESS"));
        tests.get(tests.size() - 1).actual = bPawn.movePiece(6, 4, 5, 4).name();
        tests.add(new TestCase("6.2", "movePiece(Pawn)", "6,4->4,4", "SUCCESS"));
        tests.get(tests.size() - 1).actual = bPawn.movePiece(6, 4, 4, 4).name();
        tests.add(new TestCase("6.3", "movePiece(Pawn)", "6,4->3,4", "FAIL"));
        tests.get(tests.size() - 1).actual = bPawn.movePiece(6, 4, 3, 4).name();

        // Knight
        Board bKnight = new Board(true);
        bKnight.getCell(7, 1).setPiece(new Knight(PieceColor.WHITE));
        tests.add(new TestCase("6.4", "movePiece(Knight)", "7,1->5,2", "SUCCESS"));
        tests.get(tests.size() - 1).actual = bKnight.movePiece(7, 1, 5, 2).name();
        tests.add(new TestCase("6.5", "movePiece(Knight)", "7,1->5,1", "FAIL"));
        tests.get(tests.size() - 1).actual = bKnight.movePiece(7, 1, 5, 1).name();

        // Queen
        Board bQueen = new Board(true);
        bQueen.getCell(7, 3).setPiece(new Queen(PieceColor.WHITE));
        tests.add(new TestCase("6.6", "movePiece(Queen)", "7,3->3,7", "SUCCESS"));
        tests.get(tests.size() - 1).actual = bQueen.movePiece(7, 3, 3, 7).name();
        tests.add(new TestCase("6.7", "movePiece(Queen)", "7,3->5,4", "FAIL"));
        tests.get(tests.size() - 1).actual = bQueen.movePiece(7, 3, 5, 4).name();

        // King
        Board bKing = new Board(true);
        bKing.getCell(7, 4).setPiece(new King(PieceColor.WHITE));
        tests.add(new TestCase("6.8", "movePiece(King)", "7,4->6,4", "SUCCESS"));
        tests.get(tests.size() - 1).actual = bKing.movePiece(7, 4, 6, 4).name();
        tests.add(new TestCase("6.9", "movePiece(King)", "7,4->5,4", "FAIL"));
        tests.get(tests.size() - 1).actual = bKing.movePiece(7, 4, 5, 4).name();

        // Rook
        Board bRook = new Board(true);
        bRook.getCell(7, 0).setPiece(new Rook(PieceColor.WHITE));
        tests.add(new TestCase("6.10", "movePiece(Rook)", "7,0->4,0", "SUCCESS"));
        tests.get(tests.size() - 1).actual = bRook.movePiece(7, 0, 4, 0).name();
        tests.add(new TestCase("6.11", "movePiece(Rook)", "7,0->6,1", "FAIL"));
        tests.get(tests.size() - 1).actual = bRook.movePiece(7, 0, 6, 1).name();

        // Bishop
        Board bBishop = new Board(true);
        bBishop.getCell(7, 2).setPiece(new Bishop(PieceColor.WHITE));
        tests.add(new TestCase("6.12", "movePiece(Bishop)", "7,2->3,6", "SUCCESS"));
        tests.get(tests.size() - 1).actual = bBishop.movePiece(7, 2, 3, 6).name();
        tests.add(new TestCase("6.13", "movePiece(Bishop)", "7,2->5,2", "FAIL"));
        tests.get(tests.size() - 1).actual = bBishop.movePiece(7, 2, 5, 2).name();

        // Test 7: movePieceTest for various piece types
        addMovePieceTestCases(tests);

        // Test 8: 앙파상 카운터 검증 확장
        addEnPassantTests(tests);

        // Test 9: 좌표 변환
        tests.add(new TestCase("9.1", "coordinateToNotation", "7,0", "a1"));
        tests.get(tests.size() - 1).actual = Board.coordinateToNotation(7, 0);
        tests.add(new TestCase("9.2", "notationToCoordinate", "h8", "[0,7]"));
        int[] coord = Board.notationToCoordinate("h8");
        tests.get(tests.size() - 1).actual = String.format("[%d,%d]", coord[0], coord[1]);

        // Test 10: toString 특정 행 출력
        tests.add(new TestCase("10.1", "toString", "row0", "r n b q k b n r"));
        String[] lines = b1.toString().split("\n");
        tests.get(tests.size() - 1).actual = lines[2].split("\\|")[1].trim();

        // Test 11: turnChange / getCurrentTurn
        addTurnChangeTests(tests);

        // Test 12 : CELL
        addCellTests(tests);

        // Test 13 : Pieces
        addPieceTests(tests);

        // 결과 출력
        printResults(tests);
    }

    private static void printResults(List<TestCase> tests) {
        System.out.printf("%-5s | %-20s | %-10s | %-10s | %-10s%n",
                "ID", "Function", "Input", "Expected", "Actual");
        System.out.println("-----+----------------------+------------+------------+------------");
        for (TestCase t : tests) {
            System.out.printf("%-5s | %-20s | %-10s | %-10s | %-10s%n",
                    t.id, t.function, t.input, t.expected, t.actual);
        }
    }

    private static void addNullCellTests(List<TestCase> tests, Board board) {
        tests.add(new TestCase("3.1", "getCell", "-7,4", "null"));
        tests.get(tests.size() - 1).actual = String.valueOf(board.getCell(-7, 4));
        tests.add(new TestCase("3.2", "getCell", "7,-4", "null"));
        tests.get(tests.size() - 1).actual = String.valueOf(board.getCell(7, -4));
        tests.add(new TestCase("3.3", "getCell", "-7,-4", "null"));
        tests.get(tests.size() - 1).actual = String.valueOf(board.getCell(-7, -4));
        tests.add(new TestCase("3.4", "getCell", "17,4", "null"));
        tests.get(tests.size() - 1).actual = String.valueOf(board.getCell(17, 4));
    }

    private static void addIsPathClearTests(List<TestCase> tests) {
        Board b3;
        b3 = new Board(true);
        tests.add(new TestCase("4.1", "isPathClear", "0,0->0,7", "true"));
        tests.get(tests.size() - 1).actual =
                String.valueOf(b3.isPathClear(b3.getCell(0, 0), b3.getCell(0, 7)));

        b3 = new Board(true);
        b3.getCell(0, 3).setPiece(new Pawn(PieceColor.WHITE));
        tests.add(new TestCase("4.2", "isPathClear", "0,0->0,7", "false"));
        tests.get(tests.size() - 1).actual =
                String.valueOf(b3.isPathClear(b3.getCell(0, 0), b3.getCell(0, 7)));

        b3 = new Board(true);
        tests.add(new TestCase("4.3", "isPathClear", "0,0->7,0", "true"));
        tests.get(tests.size() - 1).actual =
                String.valueOf(b3.isPathClear(b3.getCell(0, 0), b3.getCell(7, 0)));

        b3 = new Board(true);
        b3.getCell(4, 0).setPiece(new Knight(PieceColor.BLACK));
        tests.add(new TestCase("4.4", "isPathClear", "0,0->7,0", "false"));
        tests.get(tests.size() - 1).actual =
                String.valueOf(b3.isPathClear(b3.getCell(0, 0), b3.getCell(7, 0)));

        b3 = new Board(true);
        tests.add(new TestCase("4.5", "isPathClear", "0,0->7,7", "true"));
        tests.get(tests.size() - 1).actual =
                String.valueOf(b3.isPathClear(b3.getCell(0, 0), b3.getCell(7, 7)));

        b3 = new Board(true);
        b3.getCell(3, 3).setPiece(new Rook(PieceColor.BLACK));
        tests.add(new TestCase("4.6", "isPathClear", "0,0->7,7", "false"));
        tests.get(tests.size() - 1).actual =
                String.valueOf(b3.isPathClear(b3.getCell(0, 0), b3.getCell(7, 7)));

        b3 = new Board(true);
        tests.add(new TestCase("4.7", "isPathClear", "0,0->1,2", "false"));
        tests.get(tests.size() - 1).actual =
                String.valueOf(b3.isPathClear(b3.getCell(0, 0), b3.getCell(1, 2)));

        b3 = new Board(true);
        b3.getCell(4, 4).setPiece(new Pawn(PieceColor.WHITE));
        tests.add(new TestCase("4.8", "isPathClear", "4,4->7,4", "true"));
        tests.get(tests.size() - 1).actual =
                String.valueOf(b3.isPathClear(b3.getCell(4, 4), b3.getCell(7, 4)));

        b3 = new Board(true);
        tests.add(new TestCase("4.9", "isPathClear", "null->0,0", "false"));
        tests.get(tests.size() - 1).actual =
                String.valueOf(b3.isPathClear(null, b3.getCell(0, 0)));
    }

    private static void addDefaultBoardPathTests(List<TestCase> tests) {
        Board defaultBoard = new Board();
        tests.add(new TestCase("5.1", "isPathClear", "0,0->0,7", "false"));
        tests.get(tests.size() - 1).actual =
                String.valueOf(defaultBoard.isPathClear(defaultBoard.getCell(0, 0), defaultBoard.getCell(0, 7)));

        tests.add(new TestCase("5.2", "isPathClear", "2,0->2,7", "true"));
        tests.get(tests.size() - 1).actual =
                String.valueOf(defaultBoard.isPathClear(defaultBoard.getCell(2, 0), defaultBoard.getCell(2, 7)));

        tests.add(new TestCase("5.3", "isPathClear", "0,0->7,0", "false"));
        tests.get(tests.size() - 1).actual =
                String.valueOf(defaultBoard.isPathClear(defaultBoard.getCell(0, 0), defaultBoard.getCell(7, 0)));

        tests.add(new TestCase("5.4", "isPathClear", "2,0->5,0", "true"));
        tests.get(tests.size() - 1).actual =
                String.valueOf(defaultBoard.isPathClear(defaultBoard.getCell(2, 0), defaultBoard.getCell(5, 0)));

        tests.add(new TestCase("5.5", "isPathClear", "2,2->5,5", "true"));
        tests.get(tests.size() - 1).actual =
                String.valueOf(defaultBoard.isPathClear(defaultBoard.getCell(2, 2), defaultBoard.getCell(5, 5)));

        tests.add(new TestCase("5.6", "isPathClear", "0,2->3,5", "false"));
        tests.get(tests.size() - 1).actual =
                String.valueOf(defaultBoard.isPathClear(defaultBoard.getCell(0, 2), defaultBoard.getCell(3, 5)));

        tests.add(new TestCase("5.7", "isPathClear", "0,1->7,1", "true"));
        tests.get(tests.size() - 1).actual =
                String.valueOf(defaultBoard.isPathClear(defaultBoard.getCell(0, 1), defaultBoard.getCell(7, 1)));
    }

    private static void addMovePieceTestCases(List<TestCase> tests) {
        Board b7;

        b7 = new Board(true);
        b7.getCell(6, 4).setPiece(new Pawn(PieceColor.WHITE));
        tests.add(new TestCase("7.1", "movePieceTest(Pawn)", "6,4->5,4", "true"));
        tests.get(tests.size() - 1).actual = String.valueOf(b7.movePieceTest(6, 4, 5, 4));

        b7 = new Board(true);
        b7.getCell(6, 4).setPiece(new Pawn(PieceColor.WHITE));
        tests.add(new TestCase("7.2", "movePieceTest(Pawn)", "6,4->4,4", "true"));
        tests.get(tests.size() - 1).actual = String.valueOf(b7.movePieceTest(6, 4, 4, 4));

        b7 = new Board(true);
        b7.getCell(6, 4).setPiece(new Pawn(PieceColor.WHITE));
        tests.add(new TestCase("7.3", "movePieceTest(Pawn)", "6,4->3,4", "false"));
        tests.get(tests.size() - 1).actual = String.valueOf(b7.movePieceTest(6, 4, 3, 4));

        b7 = new Board(true);
        b7.getCell(7, 1).setPiece(new Knight(PieceColor.WHITE));
        tests.add(new TestCase("7.4", "movePieceTest(Knight)", "7,1->5,2", "true"));
        tests.get(tests.size() - 1).actual = String.valueOf(b7.movePieceTest(7, 1, 5, 2));

        b7 = new Board(true);
        b7.getCell(7, 1).setPiece(new Knight(PieceColor.WHITE));
        tests.add(new TestCase("7.5", "movePieceTest(Knight)", "7,1->5,1", "false"));
        tests.get(tests.size() - 1).actual = String.valueOf(b7.movePieceTest(7, 1, 5, 1));

        b7 = new Board(true);
        b7.getCell(7, 3).setPiece(new Queen(PieceColor.WHITE));
        tests.add(new TestCase("7.6", "movePieceTest(Queen)", "7,3->3,7", "true"));
        tests.get(tests.size() - 1).actual = String.valueOf(b7.movePieceTest(7, 3, 3, 7));

        b7 = new Board(true);
        b7.getCell(7, 3).setPiece(new Queen(PieceColor.WHITE));
        tests.add(new TestCase("7.7", "movePieceTest(Queen)", "7,3->5,4", "false"));
        tests.get(tests.size() - 1).actual = String.valueOf(b7.movePieceTest(7, 3, 5, 4));

        b7 = new Board(true);
        b7.getCell(7, 4).setPiece(new King(PieceColor.WHITE));
        tests.add(new TestCase("7.8", "movePieceTest(King)", "7,4->6,4", "true"));
        tests.get(tests.size() - 1).actual = String.valueOf(b7.movePieceTest(7, 4, 6, 4));

        b7 = new Board(true);
        b7.getCell(7, 4).setPiece(new King(PieceColor.WHITE));
        tests.add(new TestCase("7.9", "movePieceTest(King)", "7,4->5,4", "false"));
        tests.get(tests.size() - 1).actual = String.valueOf(b7.movePieceTest(7, 4, 5, 4));

        b7 = new Board(true);
        b7.getCell(7, 0).setPiece(new Rook(PieceColor.WHITE));
        tests.add(new TestCase("7.10", "movePieceTest(Rook)", "7,0->4,0", "true"));
        tests.get(tests.size() - 1).actual = String.valueOf(b7.movePieceTest(7, 0, 4, 0));

        b7 = new Board(true);
        b7.getCell(7, 0).setPiece(new Rook(PieceColor.WHITE));
        tests.add(new TestCase("7.11", "movePieceTest(Rook)", "7,0->6,1", "false"));
        tests.get(tests.size() - 1).actual = String.valueOf(b7.movePieceTest(7, 0, 6, 1));

        b7 = new Board(true);
        b7.getCell(7, 2).setPiece(new Bishop(PieceColor.WHITE));
        tests.add(new TestCase("7.12", "movePieceTest(Bishop)", "7,2->3,6", "true"));
        tests.get(tests.size() - 1).actual = String.valueOf(b7.movePieceTest(7, 2, 3, 6));

        b7 = new Board(true);
        b7.getCell(7, 2).setPiece(new Bishop(PieceColor.WHITE));
        tests.add(new TestCase("7.13", "movePieceTest(Bishop)", "7,2->5,2", "false"));
        tests.get(tests.size() - 1).actual = String.valueOf(b7.movePieceTest(7, 2, 5, 2));
    }

    private static void addEnPassantTests(List<TestCase> tests) {
        Board b1 = new Board(true);
        Pawn w1 = new Pawn(PieceColor.WHITE);
        b1.getCell(4, 4).setPiece(w1);
        w1.enPassantable = true; w1.enPassantCounter = 0;
        tests.add(new TestCase("8.1", "enPassantChecking", "white pawn first", "1"));
        b1.enPassantChecking();
        tests.get(tests.size()-1).actual = String.valueOf(w1.enPassantCounter);

        tests.add(new TestCase("8.2", "enPassantChecking", "white pawn second", "0"));
        b1.enPassantChecking();
        tests.get(tests.size()-1).actual = String.valueOf(w1.enPassantCounter);

        tests.add(new TestCase("8.3", "enPassantChecking", "white pawn third", "0"));
        b1.enPassantChecking();
        tests.get(tests.size()-1).actual = String.valueOf(w1.enPassantCounter);

        Board b2 = new Board(true);
        Pawn w2 = new Pawn(PieceColor.WHITE);
        b2.getCell(4, 4).setPiece(w2);
        w2.enPassantable = false; w2.enPassantCounter = 5;
        tests.add(new TestCase("8.4", "enPassantChecking", "non-enPassantable pawn", "5"));
        b2.enPassantChecking();
        tests.get(tests.size()-1).actual = String.valueOf(w2.enPassantCounter);

        Board b3 = new Board(true);
        Pawn w3 = new Pawn(PieceColor.WHITE), b3p = new Pawn(PieceColor.BLACK);
        b3.getCell(3, 3).setPiece(w3);
        b3.getCell(4, 4).setPiece(b3p);
        w3.enPassantable = true; w3.enPassantCounter = 0;
        b3p.enPassantable = true; b3p.enPassantCounter = 0;
        tests.add(new TestCase("8.5", "enPassantChecking", "two pawns first", "1,1"));
        b3.enPassantChecking();
        tests.get(tests.size()-1).actual = w3.enPassantCounter + "," + b3p.enPassantCounter;

        Board b4 = new Board(true);
        Pawn b4p = new Pawn(PieceColor.BLACK);
        b4.getCell(2, 2).setPiece(b4p);
        b4p.enPassantable = true; b4p.enPassantCounter = 0;
        tests.add(new TestCase("8.6", "enPassantChecking", "black pawn first", "1"));
        b4.enPassantChecking();
        tests.get(tests.size()-1).actual = String.valueOf(b4p.enPassantCounter);
    }

    private static void addTurnChangeTests(List<TestCase> tests) {
        Board b11 = new Board();
        tests.add(new TestCase("11.1", "turnChange", "WHITE->", "BLACK"));
        b11.turnChange();
        tests.get(tests.size() - 1).actual = b11.getCurrentTurn().name();

        Board b12 = new Board();
        b12.turnChange(); // WHITE->BLACK
        tests.add(new TestCase("11.2", "turnChange", "BLACK->", "WHITE"));
        b12.turnChange(); // BLACK->WHITE
        tests.get(tests.size() - 1).actual = b12.getCurrentTurn().name();
    }

    /** Test 12: Cell의 생성자·getter·setter·toString 테스트 */
    private static void addCellTests(List<TestCase> tests) {
        // 12.1 생성자 row 값
        Cell c1 = new Cell(2, 3);
        tests.add(new TestCase("12.1", "getRow", "", "2"));
        tests.get(tests.size()-1).actual = String.valueOf(c1.getRow());

        // 12.2 생성자 col 값
        tests.add(new TestCase("12.2", "getCol", "", "3"));
        tests.get(tests.size()-1).actual = String.valueOf(c1.getCol());

        // 12.3 초기 getPiece() → null
        tests.add(new TestCase("12.3", "getPiece", "", "null"));
        tests.get(tests.size()-1).actual = String.valueOf(c1.getPiece());

        // 12.4 초기 toString() → "."
        tests.add(new TestCase("12.4", "toString", "", "."));
        tests.get(tests.size()-1).actual = c1.toString();

        // 12.5 setPiece + getPiece 심볼 확인
        Cell c2 = new Cell(1, 1);
        Pawn p = new Pawn(PieceColor.WHITE);
        c2.setPiece(p);
        tests.add(new TestCase("12.5", "setPiece/getPiece", "", "P"));
        tests.get(tests.size()-1).actual = c2.getPiece().getSymbol();

        // 12.6 toString() with piece → "P"
        tests.add(new TestCase("12.6", "toString", "", "P"));
        tests.get(tests.size()-1).actual = c2.toString();
    }

    /**
     * Test 13: 각 Piece 서브클래스의 getSymbol() 및 isValidMove() 단위 테스트
     * ID 형식: 13.x-y (x: 기물 번호, y: 해당 기물 내 테스트 번호)
     */
    private static void addPieceTests(List<TestCase> tests) {
        Board board;

        // 13.1 Pawn
        // 13.1-1 화이트 Pawn 심볼
        tests.add(new TestCase("13.1-1", "Pawn.getSymbol", "", "P"));
        tests.get(tests.size()-1).actual = new Pawn(PieceColor.WHITE).getSymbol();
        // 13.1-2 블랙 Pawn 심볼
        tests.add(new TestCase("13.1-2", "Pawn.getSymbol", "", "p"));
        tests.get(tests.size()-1).actual = new Pawn(PieceColor.BLACK).getSymbol();
        // 13.1-3 한 칸 전진 (e2→e3)
        board = new Board(true);
        Pawn wp = new Pawn(PieceColor.WHITE);
        board.getCell(6,4).setPiece(wp);
        tests.add(new TestCase("13.1-3", "Pawn.isValidMove", "6,4->5,4", "true"));
        tests.get(tests.size()-1).actual = String.valueOf(
                wp.isValidMove(board, board.getCell(6,4), board.getCell(5,4))
        );
        // 13.1-4 두 칸 전진 (e2→e4)
        board = new Board(true);
        wp = new Pawn(PieceColor.WHITE);
        board.getCell(6,4).setPiece(wp);
        tests.add(new TestCase("13.1-4", "Pawn.isValidMove", "6,4->4,4", "true"));
        tests.get(tests.size()-1).actual = String.valueOf(
                wp.isValidMove(board, board.getCell(6,4), board.getCell(4,4))
        );
        // 13.1-5 대각선 잡기 (e2→f3 with Black Pawn)
        board = new Board(true);
        wp = new Pawn(PieceColor.WHITE);
        Pawn bp = new Pawn(PieceColor.BLACK);
        board.getCell(6,4).setPiece(wp);
        board.getCell(5,5).setPiece(bp);
        tests.add(new TestCase("13.1-5", "Pawn.isValidMove", "6,4->5,5", "true"));
        tests.get(tests.size()-1).actual = String.valueOf(
                wp.isValidMove(board, board.getCell(6,4), board.getCell(5,5))
        );

        // 13.2 Knight
        // 13.2-1 화이트 Knight 심볼
        tests.add(new TestCase("13.2-1", "Knight.getSymbol", "", "N"));
        tests.get(tests.size()-1).actual = new Knight(PieceColor.WHITE).getSymbol();
        // 13.2-2 블랙 Knight 심볼
        tests.add(new TestCase("13.2-2", "Knight.getSymbol", "", "n"));
        tests.get(tests.size()-1).actual = new Knight(PieceColor.BLACK).getSymbol();
        // 13.2-3 정상 점프 (b1→c3)
        board = new Board(true);
        Knight wn = new Knight(PieceColor.WHITE);
        board.getCell(7,1).setPiece(wn);
        tests.add(new TestCase("13.2-3", "Knight.isValidMove", "7,1->5,2", "true"));
        tests.get(tests.size()-1).actual = String.valueOf(
                wn.isValidMove(board, board.getCell(7,1), board.getCell(5,2))
        );
        // 13.2-4 잘못된 점프 (b1→b2)
        tests.add(new TestCase("13.2-4", "Knight.isValidMove", "7,1->6,1", "false"));
        tests.get(tests.size()-1).actual = String.valueOf(
                wn.isValidMove(board, board.getCell(7,1), board.getCell(6,1))
        );
        // 13.2-5 잘못된 점프 (b1→b3)
        tests.add(new TestCase("13.2-5", "Knight.isValidMove", "7,1->5,1", "false"));
        tests.get(tests.size()-1).actual = String.valueOf(
                wn.isValidMove(board, board.getCell(7,1), board.getCell(5,1))
        );

        // 13.3 Bishop
        // 13.3-1 화이트 Bishop 심볼
        tests.add(new TestCase("13.3-1", "Bishop.getSymbol", "", "B"));
        tests.get(tests.size()-1).actual = new Bishop(PieceColor.WHITE).getSymbol();
        // 13.3-2 블랙 Bishop 심볼
        tests.add(new TestCase("13.3-2", "Bishop.getSymbol", "", "b"));
        tests.get(tests.size()-1).actual = new Bishop(PieceColor.BLACK).getSymbol();
        // 13.3-3 대각선 이동 (c1→g5)
        board = new Board(true);
        Bishop wb = new Bishop(PieceColor.WHITE);
        board.getCell(7,2).setPiece(wb);
        tests.add(new TestCase("13.3-3", "Bishop.isValidMove", "7,2->3,6", "true"));
        tests.get(tests.size()-1).actual = String.valueOf(
                wb.isValidMove(board, board.getCell(7,2), board.getCell(3,6))
        );
        // 13.3-4 직선 이동 시도 (c1→c3) → 실패
        tests.add(new TestCase("13.3-4", "Bishop.isValidMove", "7,2->5,2", "false"));
        tests.get(tests.size()-1).actual = String.valueOf(
                wb.isValidMove(board, board.getCell(7,2), board.getCell(5,2))
        );

        // 13.4 Rook
        // 13.4-1 화이트 Rook 심볼
        tests.add(new TestCase("13.4-1", "Rook.getSymbol", "", "R"));
        tests.get(tests.size()-1).actual = new Rook(PieceColor.WHITE).getSymbol();
        // 13.4-2 블랙 Rook 심볼
        tests.add(new TestCase("13.4-2", "Rook.getSymbol", "", "r"));
        tests.get(tests.size()-1).actual = new Rook(PieceColor.BLACK).getSymbol();
        // 13.4-3 세로 이동 (a1→a4)
        board = new Board(true);
        Rook wr = new Rook(PieceColor.WHITE);
        board.getCell(7,0).setPiece(wr);
        tests.add(new TestCase("13.4-3", "Rook.isValidMove", "7,0->4,0", "true"));
        tests.get(tests.size()-1).actual = String.valueOf(
                wr.isValidMove(board, board.getCell(7,0), board.getCell(4,0))
        );
        // 13.4-4 대각선 이동 시도 → 실패
        tests.add(new TestCase("13.4-4", "Rook.isValidMove", "7,0->6,1", "false"));
        tests.get(tests.size()-1).actual = String.valueOf(
                wr.isValidMove(board, board.getCell(7,0), board.getCell(6,1))
        );

        // 13.5 Queen
        // 13.5-1 화이트 Queen 심볼
        tests.add(new TestCase("13.5-1", "Queen.getSymbol", "", "Q"));
        tests.get(tests.size()-1).actual = new Queen(PieceColor.WHITE).getSymbol();
        // 13.5-2 블랙 Queen 심볼
        tests.add(new TestCase("13.5-2", "Queen.getSymbol", "", "q"));
        tests.get(tests.size()-1).actual = new Queen(PieceColor.BLACK).getSymbol();
        // 13.5-3 직선 이동 (d1→d4)
        board = new Board(true);
        Queen wq = new Queen(PieceColor.WHITE);
        board.getCell(7,3).setPiece(wq);
        tests.add(new TestCase("13.5-3", "Queen.isValidMove", "7,3->4,3", "true"));
        tests.get(tests.size()-1).actual = String.valueOf(
                wq.isValidMove(board, board.getCell(7,3), board.getCell(4,3))
        );
        // 13.5-4 대각선 이동 (d1→h5)
        tests.add(new TestCase("13.5-4", "Queen.isValidMove", "7,3->3,7", "true"));
        tests.get(tests.size()-1).actual = String.valueOf(
                wq.isValidMove(board, board.getCell(7,3), board.getCell(3,7))
        );

        // 13.6 King
        // 13.6-1 화이트 King 심볼
        tests.add(new TestCase("13.6-1", "King.getSymbol", "", "K"));
        tests.get(tests.size()-1).actual = new King(PieceColor.WHITE).getSymbol();
        // 13.6-2 블랙 King 심볼
        tests.add(new TestCase("13.6-2", "King.getSymbol", "", "k"));
        tests.get(tests.size()-1).actual = new King(PieceColor.BLACK).getSymbol();
        // 13.6-3 한 칸 이동 (e1→e2)
        board = new Board(true);
        King wk = new King(PieceColor.WHITE);
        board.getCell(7,4).setPiece(wk);
        tests.add(new TestCase("13.6-3", "King.isValidMove", "7,4->6,4", "true"));
        tests.get(tests.size()-1).actual = String.valueOf(
                wk.isValidMove(board, board.getCell(7,4), board.getCell(6,4))
        );
        // 13.6-4 두 칸 이동 시도 (e1→e3) → 실패
        tests.add(new TestCase("13.6-4", "King.isValidMove", "7,4->5,4", "false"));
        tests.get(tests.size()-1).actual = String.valueOf(
                wk.isValidMove(board, board.getCell(7,4), board.getCell(5,4))
        );
    }


    /** 테스트 케이스 저장용 */
    static class TestCase {
        final String id, function, input, expected;
        String actual;
        TestCase(String id, String function, String input, String expected) {
            this.id = id; this.function = function;
            this.input = input; this.expected = expected;
        }
    }
}
