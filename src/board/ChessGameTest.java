
import java.util.Scanner;

/**
 * 각각의 오류 사항에 따른 리턴값 enum
 */
enum MoveErrorType {
    /** 시작 좌표와  도착 좌표가 동일한 경우 */
    SAME_START_END("Invalid input: Same START and END square. Try again."),
    /** 시작 위치에 기물이 없는 경우 */
    NO_PIECE_AT_START("Invalid input: No piece at start square. Try again."),
    /** 현재 턴과 다른 색상의 기물을 선택한 경우 */
    NOT_YOUR_PIECE("Invalid input: Not your piece. Try again."),
    /** 도착 위치에 자기 색상의 기물이 있는 경우 */
    OWN_PIECE_AT_DESTINATION("Invalid input: Own piece at destination. Try again."),
    /** 도착지까지의 경로가 막혀 있는 경우 (Rook, Bishop, Queen 등) */
    PATH_BLOCKED("Invalid input: Path is blocked. Try again."),
    /** 기물의 이동 규칙에 어긋난 경우 */
    INVALID_MOVE_FOR_THIS_PIECE("Invalid input: Invalid move for this piece. Try again."),

    // ===== 추가: 문법 오류 관련 =====

    /** 입력값이 비어 있거나 null인 경우 */
    EMPTY_OR_NULL("Invalid input: Empty or null. Try again."),
    /** 좌표 개수가 정확히 2개가 아닌 경우 */
    COORDINATE_COUNT("Invalid input: Exactly 2 coordinates required. Try again."),
    /** 좌표에 잘못된 문자가 포함된 경우 (예: 알파벳이 아니거나 숫자 아님) */
    INVALID_CHARACTER("Invalid input: Invalid characters in coordinates. Try again.");

    private final String message;

    MoveErrorType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

//////////////////////////////////////////////
// 1) 열거형: 기물 색상, 이동 결과
//////////////////////////////////////////////
enum PieceColor {
    WHITE,
    BLACK
}

enum MoveResult {
    SUCCESS,            // 이동 성공
    INVALID_POSITION,   // 좌표가 체스판 범위 밖
    EMPTY_CELL,         // 시작 칸에 기물이 없음
    INVALID_MOVE        // 이동 규칙 위반
}

//////////////////////////////////////////////
// 2) Cell 클래스: 체스판의 한 칸 (row, col, piece)
//////////////////////////////////////////////
class Cell {
    private int row;       // 행 번호 (0~7)
    private int col;       // 열 번호 (0~7)
    private Piece piece;   // 해당 칸에 배치된 기물 (없으면 null)

    /**
     * 생성자
     * @param row - 행 번호
     * @param col - 열 번호
     * 초기 상태에서는 기물이 없으므로 piece는 null로 설정.
     */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.piece = null;
    }

    // Getter 및 Setter
    public int getRow() { return row; }
    public int getCol() { return col; }
    public Piece getPiece() { return piece; }
    public void setPiece(Piece piece) { this.piece = piece; }

    @Override
    public String toString() {
        // 기물이 없으면 점("."), 있으면 기물의 기호 출력
        return (piece == null) ? "." : piece.getSymbol();
    }
}

//////////////////////////////////////////////
// 3) Board 클래스: 체스판 전체 관리
//////////////////////////////////////////////
class Board {
    private Cell[][] cells; // 8x8 Cell 배열

    /**
     * 기본 생성자: 새 게임을 위한 체스판 초기화 (8x8 셀 생성 후 기본 기물 배치)
     */
    public Board() {
        cells = new Cell[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                cells[row][col] = new Cell(row, col);
            }
        }
        initializeBoard();
    }

    /**
     * 세이브 파일의 정보를 반영해 보드를 재설정하는 생성자
     * @param boardState - 8줄 문자열 배열, 각 줄은 해당 행의 기물 상태를 나타냄.
     *   예: "r n b q k b n r" (기호는 공백으로 구분, "."는 빈 칸)
     */
    public Board(String[] boardState) {
        cells = new Cell[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                cells[row][col] = new Cell(row, col);
            }
        }
        loadBoardState(boardState);
    }

    /**
     * 기본 기물 배치 (새 게임 시작 시 사용)
     */
    public void initializeBoard() {
        // Black pieces (상단)
        cells[0][0].setPiece(PieceFactory.createPiece("rook", PieceColor.BLACK));
        cells[0][1].setPiece(PieceFactory.createPiece("knight", PieceColor.BLACK));
        cells[0][2].setPiece(PieceFactory.createPiece("bishop", PieceColor.BLACK));
        cells[0][3].setPiece(PieceFactory.createPiece("queen", PieceColor.BLACK));
        cells[0][4].setPiece(PieceFactory.createPiece("king", PieceColor.BLACK));
        cells[0][5].setPiece(PieceFactory.createPiece("bishop", PieceColor.BLACK));
        cells[0][6].setPiece(PieceFactory.createPiece("knight", PieceColor.BLACK));
        cells[0][7].setPiece(PieceFactory.createPiece("rook", PieceColor.BLACK));
        for (int col = 0; col < 8; col++) {
            cells[1][col].setPiece(PieceFactory.createPiece("pawn", PieceColor.BLACK));
        }

        // White pieces (하단)
        cells[7][0].setPiece(PieceFactory.createPiece("rook", PieceColor.WHITE));
        cells[7][1].setPiece(PieceFactory.createPiece("knight", PieceColor.WHITE));
        cells[7][2].setPiece(PieceFactory.createPiece("bishop", PieceColor.WHITE));
        cells[7][3].setPiece(PieceFactory.createPiece("queen", PieceColor.WHITE));
        cells[7][4].setPiece(PieceFactory.createPiece("king", PieceColor.WHITE));
        cells[7][5].setPiece(PieceFactory.createPiece("bishop", PieceColor.WHITE));
        cells[7][6].setPiece(PieceFactory.createPiece("knight", PieceColor.WHITE));
        cells[7][7].setPiece(PieceFactory.createPiece("rook", PieceColor.WHITE));
        for (int col = 0; col < 8; col++) {
            cells[6][col].setPiece(PieceFactory.createPiece("pawn", PieceColor.WHITE));
        }
    }

    /**
     * 세이브 파일의 상태 정보를 반영해 체스판을 설정합니다.
     * @param boardState - 8줄 문자열 배열, 각 줄은 해당 행의 상태를 나타냄.
     *   예: "r n b q k b n r" (기호는 공백으로 구분, "."는 빈 칸)
     */
    public void loadBoardState(String[] boardState) {
        for (int row = 0; row < 8; row++) {
            String[] tokens = boardState[row].trim().split("\\s+");
            for (int col = 0; col < 8; col++) {
                Piece piece = PieceFactory.createPieceFromSymbol(tokens[col]);
                cells[row][col].setPiece(piece);
            }
        }
    }

    public Cell getCell(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8)
            return null;
        return cells[row][col];
    }

    /**
     * 두 Cell 사이의 경로에 장애물이 없는지 검사
     * (수직, 수평, 대각선 이동 시 사용)
     */
    public boolean isPathClear(Cell start, Cell end) {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        int dRow = endRow - startRow;
        int dCol = endCol - startCol;
        int stepRow = (dRow == 0) ? 0 : dRow / Math.abs(dRow);
        int stepCol = (dCol == 0) ? 0 : dCol / Math.abs(dCol);

        int currentRow = startRow + stepRow;
        int currentCol = startCol + stepCol;

        while (currentRow != endRow || currentCol != endCol) {
            Cell cell = getCell(currentRow, currentCol);
            if (cell.getPiece() != null) {
                return false;
            }
            currentRow += stepRow;
            currentCol += stepCol;
        }
        return true;
    }

    /**
     * 기물을 이동시키는 메서드
     * 이동 후 상대 king의 체크 상태 및 en passant 상태 업데이트를 수행.
     */
    public MoveResult movePiece(int startRow, int startCol, int endRow, int endCol) {
        Cell start = getCell(startRow, startCol);
        Cell end = getCell(endRow, endCol);

        // 1) 좌표 범위 검사
        if (start == null || end == null)
            return MoveResult.INVALID_POSITION;

        Piece movingPiece = start.getPiece();
        if (movingPiece == null)
            return MoveResult.EMPTY_CELL;

        // 2) 이동 규칙 검증
        if (movingPiece.isValidMove(this, start, end)) {

            // ** (A) 도착 칸에 이미 상대 기물이 있는지 확인 **
            Piece dest = end.getPiece();
            if (dest != null && dest.getColor() != movingPiece.getColor()) {
                // 상대 기물을 잡는 상황 → 안내 메시지 출력
                System.out.println("상대방의 " + dest.getSymbol() + " 기물을 잡았습니다!");
            }

            // 3) 이동 수행 (도착 칸에 기물을 배치하고, 시작 칸은 비움)
            end.setPiece(movingPiece);
            start.setPiece(null);

            // (체크, En Passant 등 기타 후속 로직)
            PieceColor opponentColor = (movingPiece.getColor() == PieceColor.WHITE)
                    ? PieceColor.BLACK : PieceColor.WHITE;
            if (isCheck(opponentColor)) {
                System.out.println("⚠️ " + opponentColor + " king이 체크 상태입니다.");
            }
            updateEnPassantStatus();

            return MoveResult.SUCCESS;
        }

        return MoveResult.INVALID_MOVE;
    }


    /**
     * 해당 색상의 king이 체크 상태인지 확인하는 메서드
     * (구현은 추후 확장; 현재는 단순 플레이스홀더로 false 반환)
     */
    public boolean isCheck(PieceColor color) {
        // 체크 상태 확인 로직 구현 필요 – 현재는 false 반환
        return false;
    }

    /**
     * 체스판 전체의 Pawn에 대해, en passant 상태를 업데이트합니다.
     * (매 이동 후 전체 8x8 셀을 순회하며 처리)
     */
    public void updateEnPassantStatus() {
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                Piece piece = getCell(i, j).getPiece();
                if (piece != null && piece instanceof Pawn) {
                    Pawn pawn = (Pawn) piece;
                    if (pawn.enPassantable && pawn.enPassantableCount == 0) {
                        pawn.enPassantableCount = 1;
                    } else if (pawn.enPassantable && pawn.enPassantableCount == 1) {
                        pawn.enPassantable = false;
                        pawn.enPassantableCount = 0;
                    }
                }
            }
        }
    }

    /**
     * 내부 좌표 (row, col)을 체스 표기법("a1" ~ "h8")로 변환
     */
    public static String coordinateToNotation(int row, int col) {
        char file = (char) ('a' + col);
        int rank = 8 - row;
        return "" + file + rank;
    }

    /**
     * 체스 표기법("a1" ~ "h8")을 내부 좌표 (row, col) 배열로 변환
     */
    public static int[] notationToCoordinate(String notation) {
        char fileChar = notation.toLowerCase().charAt(0);
        char rankChar = notation.charAt(1);
        int col = fileChar - 'a';
        int rank = rankChar - '0';
        int row = 8 - rank;
        return new int[] { row, col };
    }

    /**
     * 체스 기물 이동 명령에 대해 의미적 오류를 판단하는 메서드입니다.
     *
     * 아래 우선순위에 따라 오류를 판별하며, 가장 중요한 의미 오류 하나만 리턴합니다:
     *  1. INVALID_MOVE_FOR_THIS_PIECE : 이동 규칙 자체 위반
     *  2. PATH_BLOCKED                 : 직선/대각선 기물의 경로에 장애물 존재
     *  3. OWN_PIECE_AT_DESTINATION     : 도착지에 같은 색 기물이 있음
     *  4. NOT_YOUR_PIECE               : 현재 턴과 다른 색의 기물을 선택
     *  5. NO_PIECE_AT_START            : 출발 위치에 기물이 없음
     *  6. SAME_START_END               : 시작과 도착 좌표가 동일
     *
     * @param fromNotation 시작 좌표 (예: "e2")
     * @param toNotation   도착 좌표 (예: "e4")
     * @param currentTurn  현재 턴 색상 (WHITE or BLACK)
     * @return MoveErrorType: 의미 오류가 있으면 그에 해당하는 타입, 없으면 null
     */
    public MoveErrorType validateMoveMeaning(String fromNotation, String toNotation, PieceColor currentTurn) {
        int[] startRC = Board.notationToCoordinate(fromNotation);
        int[] endRC = Board.notationToCoordinate(toNotation);

        int startRow = startRC[0], startCol = startRC[1];
        int endRow = endRC[0], endCol = endRC[1];

        Cell start = getCell(startRow, startCol);
        Cell end = getCell(endRow, endCol);

        Piece movingPiece = (start != null) ? start.getPiece() : null;
        Piece destPiece = (end != null) ? end.getPiece() : null;

        // 1. 시작과 끝이 같은 칸
        if (startRow == endRow && startCol == endCol) {
            return MoveErrorType.SAME_START_END;
        }

        // 2. 출발 칸에 기물이 없음
        if (movingPiece == null) {
            return MoveErrorType.NO_PIECE_AT_START;
        }

        // 3. 현재 턴과 다른 색의 기물
        if (movingPiece.getColor() != currentTurn) {
            return MoveErrorType.NOT_YOUR_PIECE;
        }

        // 4. 도착 칸에 같은 색 기물이 있음
        if (destPiece != null && destPiece.getColor() == currentTurn) {
            return MoveErrorType.OWN_PIECE_AT_DESTINATION;
        }

        // 5. 이동 규칙 위반
        if (!movingPiece.isValidMove(this, start, end)) {
            return MoveErrorType.INVALID_MOVE_FOR_THIS_PIECE;
        }

        // 6. Rook, Bishop, Queen 이동 시 경로 막힘
        boolean pathBlocked = (movingPiece instanceof Rook || movingPiece instanceof Bishop || movingPiece instanceof Queen)
                && !isPathClear(start, end);
        if (pathBlocked) {
            return MoveErrorType.PATH_BLOCKED;
        }



        return null; // 의미 오류 없음
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("==================================================\n");
        sb.append("    a b c d e f g h\n");
        sb.append("  ===================\n");

        for (int row = 0; row < 8; row++) {
            int rank = 8 - row;  // 실제 출력되는 행 번호 (8 ~ 1)
            sb.append(rank).append(" | ");
            for (int col = 0; col < 8; col++) {
                sb.append(cells[row][col].toString()).append(" ");
            }
            sb.append("| ").append(rank).append("\n");
        }

        sb.append("  ===================\n");
        sb.append("    a b c d e f g h\n");
        sb.append("--------------------------------------------------\n");

        return sb.toString();
    }
}

//////////////////////////////////////////////
// 4) Piece 추상 클래스 및 구체 기물들
//////////////////////////////////////////////
abstract class Piece {
    protected PieceColor color;

    public Piece(PieceColor color) {
        this.color = color;
    }

    public PieceColor getColor() {
        return color;
    }

    // 각 기물의 이동 가능 여부와 기호 반환은 구체 클래스에서 구현
    public abstract boolean isValidMove(Board board, Cell startCell, Cell endCell);
    public abstract String getSymbol();
}

class King extends Piece {
    public King(PieceColor color) {
        super(color);
    }

    @Override
    public boolean isValidMove(Board board, Cell startCell, Cell endCell) {
        int rowDiff = Math.abs(endCell.getRow() - startCell.getRow());
        int colDiff = Math.abs(endCell.getCol() - startCell.getCol());
        if (rowDiff <= 1 && colDiff <= 1) {
            Piece dest = endCell.getPiece();
            if (dest == null || dest.getColor() != this.color)
                return true;
        }
        return false;
    }

    @Override
    public String getSymbol() {
        return (color == PieceColor.WHITE) ? "K" : "k";
    }
}

class Queen extends Piece {
    public Queen(PieceColor color) {
        super(color);
    }

    @Override
    public boolean isValidMove(Board board, Cell startCell, Cell endCell) {
        int startRow = startCell.getRow();
        int startCol = startCell.getCol();
        int endRow = endCell.getRow();
        int endCol = endCell.getCol();
        int rowDiff = Math.abs(endRow - startRow);
        int colDiff = Math.abs(endCol - startCol);

        boolean straightMove = (startRow == endRow || startCol == endCol);
        boolean diagonalMove = (rowDiff == colDiff);

        if (straightMove || diagonalMove) {
            Piece dest = endCell.getPiece();
            if (dest == null || dest.getColor() != this.color)
                return true;
        }
        return false;
    }

    @Override
    public String getSymbol() {
        return (color == PieceColor.WHITE) ? "Q" : "q";
    }
}

class Rook extends Piece {
    public Rook(PieceColor color) {
        super(color);
    }

    @Override
    public boolean isValidMove(Board board, Cell startCell, Cell endCell) {
        if (startCell.getRow() == endCell.getRow() || startCell.getCol() == endCell.getCol()) {
            Piece dest = endCell.getPiece();
            if (dest == null || dest.getColor() != this.color)
                return true;
        }
        return false;
    }

    @Override
    public String getSymbol() {
        return (color == PieceColor.WHITE) ? "R" : "r";
    }
}

class Bishop extends Piece {
    public Bishop(PieceColor color) {
        super(color);
    }

    @Override
    public boolean isValidMove(Board board, Cell startCell, Cell endCell) {
        int rowDiff = Math.abs(endCell.getRow() - startCell.getRow());
        int colDiff = Math.abs(endCell.getCol() - startCell.getCol());
        if (rowDiff == colDiff) {
            Piece dest = endCell.getPiece();
            if (dest == null || dest.getColor() != this.color)
                return true;

        }
        return false;
    }

    @Override
    public String getSymbol() {
        return (color == PieceColor.WHITE) ? "B" : "b";
    }
}

class Knight extends Piece {
    public Knight(PieceColor color) {
        super(color);
    }

    @Override
    public boolean isValidMove(Board board, Cell startCell, Cell endCell) {
        int rowDiff = Math.abs(endCell.getRow() - startCell.getRow());
        int colDiff = Math.abs(endCell.getCol() - startCell.getCol());
        if ((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2)) {
            Piece dest = endCell.getPiece();
            if (dest == null || dest.getColor() != this.color)
                return true;
        }
        return false;
    }

    @Override
    public String getSymbol() {
        return (color == PieceColor.WHITE) ? "N" : "n";
    }
}

class Pawn extends Piece {
    // En Passant 관련 필드
    public boolean enPassantable = false;
    public int enPassantableCount = 0;

    public Pawn(PieceColor color) {
        super(color);
    }

    @Override
    public boolean isValidMove(Board board, Cell startCell, Cell endCell) {
        int startRow = startCell.getRow();
        int startCol = startCell.getCol();
        int endRow = endCell.getRow();
        int endCol = endCell.getCol();
        Piece dest = endCell.getPiece();

        int direction = (color == PieceColor.WHITE) ? -1 : 1;
        int startRowForDouble = (color == PieceColor.WHITE) ? 6 : 1;

        // 한 칸 전진 (도착 칸이 비어 있어야 함)
        if (endCol == startCol && endRow == startRow + direction && dest == null)
            return true;

        // 두 칸 전진 (초기 위치, 중간 및 도착 칸 모두 비어야 함)
        if (startRow == startRowForDouble && endCol == startCol && endRow == startRow + 2 * direction) {
            Cell intermediate = board.getCell(startRow + direction, startCol);
            if (intermediate.getPiece() == null && dest == null) {
                // 두 칸 전진 시, En Passant 대상 지정
                this.enPassantable = true;
                this.enPassantableCount = 0;
                return true;
            }
        }

        // 대각선 이동 (상대 기물 포획)
        if (Math.abs(endCol - startCol) == 1 && endRow == startRow + direction && dest != null && dest.getColor() != this.color)
            return true;

        // En Passant 공격 (인접 Pawn이 enPassantable 상태인 경우)
        if (Math.abs(endCol - startCol) == 1 && endRow == startRow + direction && dest == null) {
            Cell pawnCell = board.getCell(startRow, endCol);
            if (pawnCell != null && pawnCell.getPiece() instanceof Pawn) {
                Pawn opponentPawn = (Pawn) pawnCell.getPiece();
                if (opponentPawn.enPassantable) {
                    // En Passant 공격 시, 실제 상대 Pawn 제거 로직은 추후 구현 (여기서는 이동 가능으로 처리)
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getSymbol() {
        return (color == PieceColor.WHITE) ? "P" : "p";
    }
}

//////////////////////////////////////////////
// 5) PieceFactory: 기물 생성 전담 클래스
//////////////////////////////////////////////
class PieceFactory {
    /**
     * 기물 이름과 색상을 기반으로 해당 기물 객체를 생성합니다.
     */
    public static Piece createPiece(String pieceType, PieceColor color) {
        String lowerType = pieceType.toLowerCase();
        switch (lowerType) {
            case "king":   return new King(color);
            case "queen":  return new Queen(color);
            case "rook":   return new Rook(color);
            case "bishop": return new Bishop(color);
            case "knight": return new Knight(color);
            case "pawn":   return new Pawn(color);
            default: throw new IllegalArgumentException("유효하지 않은 기물 유형: " + pieceType);
        }
    }

    /**
     * 기물 기호를 기반으로 해당 기물 객체를 생성합니다.
     * 기호: White: K, Q, R, B, N, P / Black: k, q, r, b, n, p; 빈 칸은 "."
     */
    public static Piece createPieceFromSymbol(String symbol) {
        symbol = symbol.trim();
        if (symbol.equals(".")) return null;
        switch (symbol) {
            case "K": return new King(PieceColor.WHITE);
            case "k": return new King(PieceColor.BLACK);
            case "Q": return new Queen(PieceColor.WHITE);
            case "q": return new Queen(PieceColor.BLACK);
            case "R": return new Rook(PieceColor.WHITE);
            case "r": return new Rook(PieceColor.BLACK);
            case "B": return new Bishop(PieceColor.WHITE);
            case "b": return new Bishop(PieceColor.BLACK);
            case "N": return new Knight(PieceColor.WHITE);
            case "n": return new Knight(PieceColor.BLACK);
            case "P": return new Pawn(PieceColor.WHITE);
            case "p": return new Pawn(PieceColor.BLACK);
            default: throw new IllegalArgumentException("유효하지 않은 기물 기호: " + symbol);
        }
    }
}

//////////////////////////////////////////////
// 6) ChessGameTest: 게임 실행 및 사용자 입력 관리
//////////////////////////////////////////////
public class ChessGameTest {
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
            board = new Board(savedBoard);
            System.out.println("세이브 파일 불러오기 완료!");
        } else {
            // 새 게임 시작
            board = new Board();
            currentTurn = PieceColor.WHITE;
        }



        // 게임 루프
        while (true) {
            // 초기 상태 출력
            System.out.println(board);
            System.out.print(currentTurn + " > ");
            String input = scan.nextLine().trim();

            // 1. 입력 비었는지 확인
            if (input == null || input.isBlank()) {
                printError(MoveErrorType.EMPTY_OR_NULL); // ← 문법 오류지만 예외적으로 같이 씀
                continue;
            }

            // 2. 좌표 개수 확인
            String[] tokens = input.split("\\s+");
            if (tokens.length != 2) {
                printError(MoveErrorType.COORDINATE_COUNT); // ← 역시 문법 오류 범주
                continue;
            }

            String startNotation = tokens[0];
            String endNotation = tokens[1];

            // 3. 좌표 형식 검사
            if (!isValidNotation(startNotation) || !isValidNotation(endNotation)) {
                printError(MoveErrorType.INVALID_CHARACTER);
                continue;
            }

            // 4. 의미 오류 검사 (보드 상태 기반)
            MoveErrorType error = board.validateMoveMeaning(startNotation, endNotation, currentTurn);
            if (error != null) {
                printError(error);
                continue;
            }

            // 5. 의미 오류 없음 → 이동 수행
            int[] startRC = Board.notationToCoordinate(startNotation);
            int[] endRC = Board.notationToCoordinate(endNotation);
            MoveResult result = board.movePiece(startRC[0], startRC[1], endRC[0], endRC[1]);

            if (result == MoveResult.SUCCESS) {
                System.out.println("✅ 이동 성공!");
                System.out.println(board);
                currentTurn = (currentTurn == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
            } else {
                // (보통 여기 안 와야 함)
                printError(MoveErrorType.INVALID_MOVE_FOR_THIS_PIECE);
            }
        }
        // scan.close(); // 무한 루프로 종료되지 않으므로 닫지 않습니다.
    }

    /**
     * enum InputError 값에 맞는 오류 출력
     * @param error
     */
    static void printError(MoveErrorType error) {
        System.out.println("==================================================");
        System.out.println(error.getMessage());
        System.out.println("==================================================");
        System.out.println();
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
