package gameManager;

import data.*;

import Menu.Menu;
import fileManager.*;
import board.Board;

import Input.GameInput;
import userinput.UserInput;
import Menu.MenuInput;


public class GameManager {

    private final int ERRORCODE = GameInputReturn.ERROR.getCode();
    private final int HELPCODE = GameInputReturn.HELP.getCode();
    private final int EXITCODE = GameInputReturn.EXIT.getCode();
    private final int STARTCODE = GameInputReturn.START.getCode();
    private final int QUITCODE = GameInputReturn.QUIT.getCode();
    private final int SAVECODE = GameInputReturn.SAVE.getCode();
    private final int LOADCODE = GameInputReturn.LOAD.getCode();
    private final int DELSAVECODE = GameInputReturn.DEL_SAVE.getCode();
    private final int SAVEFILECODE = GameInputReturn.SAVE_FILE.getCode();
    private final int COORDINATECODE = GameInputReturn.COORDINATE_TRUE.getCode();




    public boolean isPlaying = false;
    public boolean isRunning = true;
    public boolean isSaved = false;
    private PieceColor playerTurn;

    private FileManager fileManager;
    private FilePrint filePrint;
    private final Menu menu;
    private Board board;

    public GameManager() {
        fileManager = FileManager.getInstance();
        menu = new Menu();

        runProgram();

    }


    private int runProgram(){
        while(isRunning){
            int cmdCode = 0;
            if(isPlaying){
                cmdCode = runGame();
            }else{
                cmdCode = runMenu();
            }
            if(cmdCode == COORDINATECODE) {
                //좌표입력시 받을 int return 값, 아직 값 미확정
            }else{
                runCommand(cmdCode);
            }
        }

        return 0;
    }

    private int runGame(){
        printGame();
        int input = GameInput.gameInput();
        if(input == COORDINATECODE){ //좌표 입력 시 -> 아직 미완성임!!!!!!!
            String fromNotation = UserInput.getFromNotation();
            String toNotation = UserInput.getToNotation();
            // 시작 좌표, 끝 좌표 String 얻는 메서드 필요
            int[] start;
            int[] end;
            start = Board.notationToCoordinate(fromNotation);
            end = Board.notationToCoordinate(toNotation);

            if(start.length > 2 || end.length > 2){
                throw new IllegalArgumentException("Failed to handle String coordinates");
            }

            boolean moveSuccess = board.movePiece(start[0], start[1], end[0], end[1]);
            if(moveSuccess){
                playerTurn = (playerTurn == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
            }else{

            }
        }else{
            //input으로 cmdRun
            //여기서 input이 isRunning 또는 isPlaying에 영향을 줄 경우 return값 받아 사용해야함.
        }


        return 0;
    }

    private void printGame(){
        String gameStr = board.toString();
        System.out.println(PrintTemplate.BOLDLINE);
        System.out.print(gameStr);
        System.out.println(PrintTemplate.INTERLINE);
        System.out.println(PrintTemplate.GAME_BASE_INSTRUCT);
        System.out.println(PrintTemplate.BOLDLINE);

        //플레이어 턴에 따른 프롬프트 구문
        if(playerTurn == PieceColor.WHITE)
            System.out.println(PrintTemplate.WHITE_PROMPT);
        else if(playerTurn == PieceColor.BLACK)
            System.out.println(PrintTemplate.BLACK_PROMPT);
    }

    private int runMenu(){
        menu.printDefaultMenu();

        return 0;
    }

    //runCommand
    private int runCommand(int cmdCode){

        if(cmdCode == HELPCODE){
            System.out.println(PrintTemplate.BOLDLINE);
            System.out.println(Command.HELP);
            System.out.println(PrintTemplate.BOLDLINE + "\n");
        }
        if(cmdCode == EXITCODE){
            if(isPlaying){
                if(isSaved){

                }else{

                }
                if(playerTurn == PieceColor.WHITE)
                    System.out.println(PrintTemplate.WHITE_PROMPT);
                else if(playerTurn == PieceColor.BLACK)
                    System.out.println(PrintTemplate.BLACK_PROMPT);
            }else{
                System.out.println(PrintTemplate.MENU_PROMPT);

            }
            System.out.print(PrintTemplate.MENU_PROMPT);
            System.out.print(Command.YES_OR_NO_EXIT);
            boolean input = MenuInput.yesornoInput();
            if(input){
                menu.printWithTemplate(Command.EXIT.toString());
            }else{

            }
        }
        if(cmdCode == STARTCODE){

            playerTurn = PieceColor.WHITE;

        }
        if(cmdCode == QUITCODE){

        }
        if(cmdCode == SAVECODE){
            filePrint.saveFilePrint(MenuInput.number);

        }
        if(cmdCode == LOADCODE) {
            boolean isLoad = fileManager.loadSavedFile(MenuInput.number);
            if (isLoad) {
//                    menuStr = Command.LOAD.formatStr(MenuInput.number, MenuInput.number, "");
//                }else{
//                    menuStr = FileError.FAILED_LOAD.toString();
//                }
            }
        }
        if (cmdCode == DELSAVECODE) {
                filePrint.deleteFilePrint(MenuInput.number);

        }
        if (cmdCode == SAVEFILECODE) {
                filePrint.saveListPrint();

        }

        return 0;
    }
}
