import Input.GameInput;
import board.Board;
import data.GameInputReturn;
import Input.UserInput;

public class TestMain {

    public static void main(String[] args) {

        Board board = new Board();
        while(true) {
            System.out.println(board);
            if(GameInput.gameInput()== GameInputReturn.COORDINATE_TRUE.getCode()){
                UserInput.getFromRow();
                board.movePiece(UserInput.getFromRow(),UserInput.getFromCol(),UserInput.getToRow(),UserInput.getToCol());
            };

        }

    }
}
