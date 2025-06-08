
package board;

import data.PieceColor;
import piece.*;

// 5) PieceFactory: 기물 생성 전담 클래스 - ChessGameTest에서 가져옴.
public class PieceFactory {
    /**
     * 기물 이름과 색상을 기반으로 해당 기물 객체를 생성합니다.
     */
    public static Piece createPiece(String pieceType, PieceColor color) {
        String lowerType = pieceType.toLowerCase();
        return switch (lowerType) {
            case "king" -> new King(color);
            case "queen" -> new Queen(color);
            case "rook" -> new Rook(color);
            case "bishop" -> new Bishop(color);
            case "knight" -> new Knight(color);
            case "pawn" -> new Pawn(color);
            default -> throw new IllegalArgumentException("유효하지 않은 기물 유형: " + pieceType);
        };
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
            case "G": return new Gaza(PieceColor.WHITE);
            case "g": return new Gaza(PieceColor.BLACK);
            case "M": return new Mantri(PieceColor.WHITE);
            case "m": return new Mantri(PieceColor.BLACK);
            case "F": return new Fawn(PieceColor.WHITE);
            case "f": return new Fawn(PieceColor.BLACK);
            case "Z": return new Pawn2(PieceColor.WHITE);
            case "z": return new Pawn2(PieceColor.BLACK);
            default: throw new IllegalArgumentException("유효하지 않은 기물 기호: " + symbol);
        }
    }
}
