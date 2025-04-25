package data;

/**
 * main 메소드의 while 문에서 입력 오류(문법 규칙, 의미 규칙 위반)는 MoveErrorType에서 정의
 * MoveResult는 입력이 문제가 없어 이동에 성공한 경우 메시지 출력, 규칙에 위반한 경우는 모든 경우를 FAIL로 통일
 */
public enum MoveResult {
    SUCCESS("Moved Successfully"),
    FAIL;

    private final String message;

    // 기본 생성자: 메시지 없는 경우 (FAIL에 해당)
    MoveResult() {
        this.message = null;
    }

    // 메시지 있는 경우 (SUCCESS에 해당)
    MoveResult(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return (message != null) ? message : name();
    }
}
