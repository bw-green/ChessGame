package piece;

import board.Board;
import board.Cell;
import data.PieceColor;

//////////////////////////////////////////////

public abstract class Piece {
    protected PieceColor color;  // 기물의 색상

    /**
     * 생성자
     * @param color - 기물의 색상 (입력: data.PieceColor)
     */
    public Piece(PieceColor color) {
        this.color = color;
    }

    // 0416 update - 각 기물들에 deepcopy 메소드와 생성자 추가함.
    // deep copy 메소드에 대한 추상 선언(명시적)
    public abstract Piece deepCopy();

    /**
     * 기물의 색상을 반환합니다.
     * @return data.PieceColor
     */
    public PieceColor getColor() {
        return color;
    }

    /**
     * 기물이 시작 Cell에서 도착 Cell으로 이동 가능한지 여부를 판단.
     * @param board     - 현재 체스판
     * @param startCell - 이동 시작 위치
     * @param endCell   - 이동 도착 위치
     * @return true if 이동 유효, false otherwise
     */
    public abstract boolean isValidMove(Board board, Cell startCell, Cell endCell);

    /**
     * 기물의 문자 표기를 반환합니다. (예: "K", "Q", "R", "B", "N", "P")
     * @return String (기물 기호)
     */
    public abstract String getSymbol();

}
