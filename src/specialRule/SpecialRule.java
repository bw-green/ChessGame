package specialRule;

import java.util.Scanner;
import board.Board;
import board.Cell;
import data.InvalidCoordinate;
import data.PrintTemplate;
import data.Unspecified;
import piece.Piece;
import piece.King;
import piece.Rook;
import piece.Queen;
import piece.Knight;
import piece.Bishop;
import data.PieceColor;

public class SpecialRule {
    /*
     * 앙파상 처리 // 턴 관리 문제 있음
     * @param board 체스판 객체
     * @param start 시작 위치 셀
     * @param end 도착 위치 셀
     * @return boolean값
     */
    public static boolean enPassant(Cell start, Cell end,Cell enPassant){
        Piece movingPiece = start.getPiece(); //움직이는 piece고, 폰이다.
        Piece targetPiece = enPassant.getPiece(); // 앙파상 당할 모든 자격을 갖춘 piece다.
        if (targetPiece.getColor() != movingPiece.getColor()){  //색깔이 달라야한다.
            enPassant.setPiece(null); //캡쳐당하는 기물과 실제 이동 기물의 위치가 다르므로, 특수룰에서 캡쳐된 기물을 제거해준다.
            System.out.println(PrintTemplate.BOLDLINE);
            System.out.println("EnPassant Success");
            return true;
        }
        return false;
    }

    /**
     * 프로모션 처리
     * @param end 도착 위치 셀
     */
    public static void promotion(Cell end) {
        Scanner scanner = new Scanner(System.in);
        Piece pawn = end.getPiece(); //폰 확정은 아니지만 일단 이름은 pawn
        int targetEndRow = (pawn.getColor() == PieceColor.WHITE) ? 0 : 7; // 판별할 끝을 정하기
        if (("P".equals(pawn.getSymbol())||"p".equals(pawn.getSymbol())) && end.getRow() == targetEndRow) //폰이면서, 끝랭크인지 확인
        {
            while(true) {
                System.out.print("Enter the PIECE to promote to (e.g., \"Q\", \"R\", \"N\", \"B\").\n ");
                char promoteName = scanner.next().charAt(0);
                Piece newPiece = switch (promoteName) {
                    case 'q', 'Q' -> new Queen(pawn.getColor());
                    case 'r', 'R' -> new Rook(pawn.getColor());
                    case 'b', 'B' -> new Bishop(pawn.getColor());
                    case 'n', 'N' -> new Knight(pawn.getColor());
                    default -> null;
                };

                if (newPiece != null) {
                    end.setPiece(newPiece);
                    System.out.println("Promote success");
                    break;
                } else if (promoteName == 'p' || promoteName == 'P') {
                    System.out.println(PrintTemplate.BOLDLINE);
                    System.out.println(InvalidCoordinate.PROMO_NOT_PAWN);
                } else if (promoteName == 'k' || promoteName == 'K') {
                    System.out.println(PrintTemplate.BOLDLINE);
                    System.out.println(InvalidCoordinate.PROMO_NOT_KING);
                } else {
                    System.out.println(PrintTemplate.BOLDLINE);
                    System.out.println(InvalidCoordinate.PROMO_INVALID_PIECE);
                }
            }
        }
    }

    /**
     * 캐슬링 처리
     * @param board 체스판 객체
     * @param kingStart 킹 시작 위치
     * @param kingEnd 킹 도착 위치
     * @return boolean
     */
    public static boolean castling(Board board, Cell kingStart, Cell kingEnd) {
        //어느쪽으로 캐슬링하려는 건지 판단
        Piece king = kingStart.getPiece(); //킹부터 확보
        int colDiff = kingEnd.getCol() - kingStart.getCol(); //절댓값이 아니기에 양수면 h 파일쪽, 음수면 a 파일쪽
        int rookCol = (colDiff > 0) ? 7 : 0; // h파일인지 a 파일인지 확인
        Cell rookEndCell; //미리 선언
        //캐슬링하려는 방향의 룩이 있다는 가정하에 룩의 시작셀, 끝셀 선언
        Cell rookCell = board.getCell(kingStart.getRow(), rookCol); //룩이 있다고 가정하에 셀을 획득
        if(rookCol == 7)//아까 정한 rookcol에 따른 룩의 도착지점 정하기
            rookEndCell = board.getCell(kingStart.getRow(), rookCol - 2);
        else
            rookEndCell = board.getCell(kingStart.getRow(), rookCol + 3);
        //룩 셀에 룩이 없거나, 길이 열려있지 않으면, 실패. 룩이나 킹이 한번이라도 움직였어도 실패.
        Piece movingPiece = rookCell.getPiece();
        if (movingPiece == null||!("R".equals(movingPiece.getSymbol())||"r".equals(movingPiece.getSymbol())) || !board.isPathClear(kingStart, rookCell) || ((Rook) movingPiece).firstMove || ((King)king).firstMove) {
            System.out.println(PrintTemplate.BOLDLINE);
            System.out.println(Unspecified.CASTLING_FAILED);
            return false;
        }
        //룩을 이동시킴 (true 반환하면 king은 보드 클래스에서 움직임)
        rookEndCell.setPiece(movingPiece);
        rookCell.setPiece(null);
        System.out.println(PrintTemplate.BOLDLINE);
        System.out.println("Castling Success");
        return true;
    }
}
