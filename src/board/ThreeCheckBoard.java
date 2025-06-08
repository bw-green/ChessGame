//쓰리체크
package board;

public class ThreeCheckBoard extends Board{
    public int ThreeCheckW = 0;
    public int ThreeCheckB = 0;


    public ThreeCheckBoard(boolean canEnpassant, boolean canCastling, boolean canPromotion, boolean initialize){
        super(true, true, true, initialize);
    }

}