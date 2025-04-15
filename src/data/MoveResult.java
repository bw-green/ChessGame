package data;

public enum MoveResult {
    SUCCESS,            // 이동 성공
    INVALID_POSITION,   // 좌표가 체스판 범위 밖
    EMPTY_CELL,         // 시작 칸에 기물이 없음
    INVALID_MOVE        // 이동 규칙 위반
}
