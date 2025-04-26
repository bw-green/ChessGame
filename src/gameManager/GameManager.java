package gameManager;

import data.*;

import Menu.Menu;
import fileManager.*;
import board.Board;

import Input.GameInput;
import Input.UserInput;
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
        filePrint = new FilePrint(fileManager);
        menu = new Menu(filePrint);

        runProgram();

    }

    public static void main(){
        GameManager gameManager = new GameManager();
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
            }else if(cmdCode >= HELPCODE && cmdCode <= SAVEFILECODE){
                runCommand(cmdCode);
            }
        }

        return 0;
    }

    public void testCmd(int cmdCode){
        runCommand(cmdCode);
    }

    private int runGame(){
        printGame();
        int input = GameInput.gameInput();
        if(input == COORDINATECODE){
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

            MoveResult moveSuccess = board.movePiece(start[0], start[1], end[0], end[1]);
            if(moveSuccess == MoveResult.SUCCESS){
                board.turnChange();
                playerTurn = board.getCurrentTurn();
                isSaved = false;
            }else{

            }
        }else{
            return input;
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
            System.out.print(PrintTemplate.WHITE_PROMPT);
        else if(playerTurn == PieceColor.BLACK)
            System.out.print(PrintTemplate.BLACK_PROMPT);
    }

    private int runMenu(){
        menu.printDefaultMenu();
        return MenuInput.menuInput();
    }

    public void showSaveAndList(){
        System.out.println(PrintTemplate.BOLDLINE);
        filePrint.showFileList();
        if(isSaved)
            System.out.println(PrintTemplate.GAME_SAVED);
        else
            System.out.println(PrintTemplate.GAME_NOT_SAVED);
        System.out.println(PrintTemplate.BOLDLINE);
        if(playerTurn == PieceColor.WHITE)
            System.out.print(PrintTemplate.WHITE_PROMPT);
        else if(playerTurn == PieceColor.BLACK)
            System.out.print(PrintTemplate.BLACK_PROMPT);
    }

    //runCommand
    private void runCommand(int cmdCode){


        if(cmdCode == HELPCODE){
            System.out.println(PrintTemplate.BOLDLINE);
            System.out.println(Command.HELP);
            System.out.println(PrintTemplate.BOLDLINE + "\n");
        }


        if(cmdCode == EXITCODE){
            if(isPlaying){
                showSaveAndList();
            }else{
                System.out.print(PrintTemplate.MENU_PROMPT);
            }

            System.out.print(Command.YES_OR_NO_EXIT);
            boolean input = MenuInput.yesornoInput();
            if(input){
                menu.printWithTemplate(Command.EXIT.toString());
                System.exit(0);
            }
        }


        if(cmdCode == STARTCODE){
            if(!isPlaying){
                playerTurn = PieceColor.WHITE;
                isPlaying = true;
                board = new Board();
            }else{
                System.out.println(PrintTemplate.BOLDLINE);
                System.out.println(CommandError.START_BLOCK);
                System.out.println(PrintTemplate.BOLDLINE);
            }
        }

        if(cmdCode == QUITCODE){
            if(isPlaying){
                showSaveAndList();

                System.out.print(Command.YES_OR_NO_EXIT);
                boolean input = MenuInput.yesornoInput();
                if(input){
                    menu.printWithTemplate(Command.EXIT.toString());
                    isPlaying = false;
                }
            }
        }

        // TODO : Numbering issue with playing Game + zazal han error
        if(cmdCode == SAVECODE){
            int slot;
            if(!isPlaying){
                board = new Board();
                slot = MenuInput.number;
            }else { slot = GameInput.number; }
            isSaved = fileManager.overWriteSavedFile(slot, board);
            if(isSaved){
                System.out.println(PrintTemplate.BOLDLINE);
                filePrint.showFileList();
                System.out.println(FileMessage.SAVE_CREATED.format(slot));
                System.out.println(PrintTemplate.BOLDLINE + "\n");
            }else{
                System.out.println(PrintTemplate.BOLDLINE);
                System.out.println(FileError.FAILED_SAVE);
                System.out.println(PrintTemplate.BOLDLINE + "\n");
            }
        }

        if(cmdCode == LOADCODE) {
            int slot;
            if(isPlaying){
                showSaveAndList();
                System.out.print(Command.YES_OR_NO_EXIT);
                boolean input = MenuInput.yesornoInput();
                if(!input){ return; }
                slot = GameInput.number;
            }else{
                slot = MenuInput.number;
            }
            board = new Board();
            boolean isLoad = fileManager.loadSavedFile(slot, board);
            if (isLoad) {
                playerTurn = board.getCurrentTurn();
                isPlaying = true;
                System.out.println(PrintTemplate.BOLDLINE);
                System.out.println(FileMessage.SAVE_LOADED.format(slot));
                System.out.println(PrintTemplate.BOLDLINE + "\n");
            }else{
                System.out.println(PrintTemplate.BOLDLINE);
                System.out.println(FileError.FAILED_LOAD);
                System.out.println(PrintTemplate.BOLDLINE+ "\n");
            }
        }

        if (cmdCode == DELSAVECODE) {
            if(!isPlaying){
                filePrint.deleteFilePrint(MenuInput.number);
            }else{
                System.out.println(CommandError.DELSAVE_BLOCK);
            }
        }

        if (cmdCode == SAVEFILECODE) {
            filePrint.saveListPrint();

        }

    }
}
