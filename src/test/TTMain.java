package test;


import data.PieceColor;
import piece.Fawn;
import piece.Mantri;
import piece.Piece;

import java.awt.*;

public class TTMain {



    public static void main(String[] args) {
        Piece piece = new Mantri(PieceColor.WHITE);
        upstream(piece);
        if(piece instanceof Fawn){
            System.out.println("Fawn");
        }
    }

    private static void upstream(Piece piece) {
        piece = new Fawn(PieceColor.WHITE);
    }
}