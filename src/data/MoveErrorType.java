package data;

public enum MoveErrorType {
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
