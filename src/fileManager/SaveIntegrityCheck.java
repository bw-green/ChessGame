package fileManager;

import board.Board;
import board.Chaturanga;
import board.ThreeCheckBoard;

import java.util.List;

public class SaveIntegrityCheck {
    private final List<String> lines;
    private List<String> errors;

    public SaveIntegrityCheck(List<String> lines) {
        this.lines = lines;
        validate();
    }

    public Board validate(){

        if(checkKeyValueBlock(lines)) return null; //1
        if(findBoardStartIndex(lines)) return null; //2
        if(checkBoardLines(lines, 0)) return null; //3 //start 임시값 대입
        if(checkPieceSymbols()) return null; //4
        if(checkKingCount()) return null; //5
        if(checkRuleFlags()) return null; //6
        if(checkThreeCheckSettings()) return null; //7
        if(checkPieceCoordinates()) return null; //8






        Board board;
        String gameType = lines.get(2);
        switch(gameType){
            case "1": //표준체스
                board = new Board();
                break;
            case "2": //쓰리체크
                board = new ThreeCheckBoard(true,true,true,true);
                break;
            case "3": //차투랑가
                board = new Chaturanga(false,false,true,true);
                break;
            case "4": //폰게임
                board = new Board();
                break;
            default:
                board = new Board();
                errors.add("Invalid game type");
                break;
        }


        if(checkGameEnd()) return null; //9



        return board;
    }

    public List<String> getErrors() {
        return errors;
    }

    private boolean checkKeyValueBlock(List<String> lines){
        return true;

    }

    private boolean findBoardStartIndex(List<String> lines){
        return true;

    }

    private boolean checkBoardLines(List<String> lines, int start){
        return true;

    }

    private boolean checkPieceSymbols(){
        return true;

    }

    private boolean checkKingCount(){
        return true;

    }

    private boolean checkRuleFlags(){
        return true;

    }

    private boolean checkThreeCheckSettings(){
        return true;

    }

    private boolean checkPieceCoordinates(){
        return true;

    }

    //보드 생성 후 무결성 검사

    private boolean checkGameEnd(){
        return true;

    }


}
