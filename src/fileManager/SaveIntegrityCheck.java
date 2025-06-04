package fileManager;

import board.Board;

import java.util.List;

public class SaveIntegrityCheck {
    private final List<String> lines;
    private List<String> errors;

    public SaveIntegrityCheck(List<String> lines) {
        this.lines = lines;
        validate();
    }

    public Board validate(){









        Board board;
        String gameType = lines.get(2);
        switch(gameType){
            case "1": //표준체스
                board = new Board();
                break;
            case "2": //쓰리체크
                board = new Board();
                break;
            case "3": //차투랑가
                board = new Board();
                break;
            case "4": //폰게임
                board = new Board();
                break;
            default:
                board = new Board();
                errors.add("Invalid game type");
                break;
        }






        return board;
    }

    public List<String> getErrors() {
        return errors;
    }



}
