package Input;

import data.GameInputReturn;
import data.InvalidCoordinate;

/**
 * UserInput 클래스
 * - GameInput 클래스에서 '/' 없이 들어온 사용자 입력(좌표 문자열)을 넘겨받아
 *   해당 좌표가 문법적으로 유효한지 검사하고, 내부에 from/to 좌표를 저장한다.
 *   문법 오류 발생 시, InvalidCoordinate enum 기반 메시지를 출력한다.
 * - 의미 검증(기물이 있는지 등)은 GameManager 또는 Board 클래스에서 수행한다.
 */
public class UserInput {
    private static int fromRow, fromCol;
    private static int toRow, toCol;
    private static String fromNotation;
    private static String toNotation;

    /**
     * 전달된 좌표 문자열을 파싱하고 문법 유효성을 검사한 뒤, 내부 좌표로 변환
     * @param input 사용자로부터 전달된 좌표 문자열 (예: "e2 e4")
     * @return 유효한 입력이면 COORDINATE_TRUE, 그렇지 않으면 ERROR
     */
    public static int handleInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            printError(InvalidCoordinate.EMPTY_INPUT);
            return GameInputReturn.ERROR.getCode();
        }

        String[] tokens = input.trim().split("\\s+");
        if (tokens.length != 2) {
            printError(InvalidCoordinate.INCORRECT_COUNT);
            return GameInputReturn.ERROR.getCode();
        }

        String from = tokens[0].toLowerCase();
        String to = tokens[1].toLowerCase();

        if (from.length() != 2 || to.length() != 2) {
            printError(InvalidCoordinate.INCORRECT_LENGTH);
            return GameInputReturn.ERROR.getCode();
        }

        if (hasInnerSpace(from) || hasInnerSpace(to)) {
            printError(InvalidCoordinate.SPACE_IN_COORDINATES);
            return GameInputReturn.ERROR.getCode();
        }

        if (!isValidNotation(from) || !isValidNotation(to)) {
            printError(InvalidCoordinate.INVALID_RANGE);
            return GameInputReturn.ERROR.getCode();
        }

        // 좌표값 저장
        fromRow = 8 - Character.getNumericValue(from.charAt(1));
        fromCol = from.charAt(0) - 'a';
        toRow = 8 - Character.getNumericValue(to.charAt(1));
        toCol = to.charAt(0) - 'a';

        fromNotation = from;
        toNotation = to;

        return GameInputReturn.COORDINATE_TRUE.getCode();
    }

    private static boolean hasInnerSpace(String pos) {
        return pos.contains(" ") || pos.contains("\u00A0") || pos.contains("\t");
    }

    /**
     * a1 ~ h8 범위인지 확인
     */
    private static boolean isValidNotation(String pos) {
        return pos.charAt(0) >= 'a' && pos.charAt(0) <= 'h' &&
                pos.charAt(1) >= '1' && pos.charAt(1) <= '8';
    }

    private static void printError(InvalidCoordinate error) {
        System.out.println("==================================================");
        System.out.println("Invalid input: " + error);
        System.out.println("==================================================");
    }

    // getter methods
    public static int getFromRow() { return fromRow; }
    public static int getFromCol() { return fromCol; }
    public static int getToRow() { return toRow; }
    public static int getToCol() { return toCol; }
    public static String getFromNotation() { return fromNotation; }
    public static String getToNotation() { return toNotation; }

    /**
     * "e2 e4" 형태로 움직임을 문자열로 반환 (FileManager 기록용)
     */
    public static String toStringMove() {
        return fromNotation + " " + toNotation;
    }
}
