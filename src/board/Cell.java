package board;

import piece.Piece;

//////////////////////////////////////////////
public class Cell {
    private final int row;       // 행 번호 (0~7)
    private final int col;       // 열 번호 (0~7)
    private Piece piece;   // 해당 칸에 있는 기물 (없으면 null)

    /**
     * 생성자
     * @param row - Cell의 행 번호 (입력: int)
     * @param col - Cell의 열 번호 (입력: int)
     * 초기 상태에서는 기물이 없으므로 piece는 null로 설정.
     */

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.piece = null;
    }

    // Getter 메서드들
    public int getRow() { return row; }
    public int getCol() { return col; }
    public Piece getPiece() { return piece; }

    /**
     * 기물을 해당 칸에 배치합니다.
     * @param piece - 배치할 기물 (입력: piece.Piece)
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    @Override
    public String toString() {
        // 기물이 없으면 점("."), 있으면 기물 기호를 출력
        return (piece == null) ? "." : piece.getSymbol();
    }
}
