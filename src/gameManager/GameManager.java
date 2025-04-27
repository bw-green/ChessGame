package gameManager;

import check.GameEnd;
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

    private boolean isMenuPrint = true;
    private boolean isGamePrint = true;
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

    private void runProgram(){
        while(isRunning){
            int cmdCode;
            if(isPlaying){
                cmdCode = runGame();
            }else{
                cmdCode = runMenu();
            }
            if(cmdCode >= HELPCODE && cmdCode <= SAVEFILECODE){
                runCommand(cmdCode);
            }else if(cmdCode == ERRORCODE){
                if(!isPlaying) isMenuPrint = false;
            }
        }
    }

    private int runGame(){
        if(isGamePrint){ printGame(); }

        if(playerTurn == PieceColor.WHITE) { System.out.print(PrintTemplate.WHITE_PROMPT); }
        else if(playerTurn == PieceColor.BLACK) { System.out.print(PrintTemplate.BLACK_PROMPT); }

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
                //게임 승패 먼저 판정
                isGameEnd(PieceColor.BLACK);
                isGameEnd(PieceColor.WHITE);
                //턴 전환
                if(isPlaying){
                    board.turnChange();
                    playerTurn = board.getCurrentTurn();
                    isSaved = false;
                    isGamePrint = true;
                }
                else{ //게임이 종료 되었을 때 보드 출력
                    String gameStr = board.toString();
                    System.out.println(PrintTemplate.BOLDLINE);
                    System.out.print(gameStr);
                    System.out.println(PrintTemplate.BOLDLINE +"\n");
                }
            }


        }else{ return input; }


        return -1;
    }

    private void isGameEnd(PieceColor pieceColor){
        GameEnd gameEnd = new GameEnd(pieceColor);
        if(gameEnd.isCheckMate(board)){
            System.out.println(PrintTemplate.BOLDLINE);
            if(pieceColor == PieceColor.WHITE) { System.out.println(PrintTemplate.END_BLACK_CHECKMATE); }
            else { System.out.println(PrintTemplate.END_WHITE_CHECKMATE); }
            System.out.println(PrintTemplate.BOLDLINE + "\n");
            isPlaying = false;
            isMenuPrint = true;
        }else if(gameEnd.isStaleMate(board)){
            System.out.println(PrintTemplate.BOLDLINE);
            System.out.println(PrintTemplate.END_STALEMATE);
            System.out.println(PrintTemplate.BOLDLINE + "\n");
            isPlaying = false;
            isMenuPrint = true;
        }else if(gameEnd.isInsufficientPieces(board)){
            System.out.println(PrintTemplate.BOLDLINE);
            System.out.println(PrintTemplate.END_INSUFFICIENT);
            System.out.println(PrintTemplate.BOLDLINE + "\n");
            isPlaying = false;
            isMenuPrint = true;
        }
    }

    private void printGame(){
        String gameStr = board.toString();
        System.out.println(PrintTemplate.BOLDLINE);
        System.out.print(gameStr);
        System.out.println(PrintTemplate.INTERLINE);
        System.out.println(PrintTemplate.GAME_BASE_INSTRUCT);
        System.out.println(PrintTemplate.BOLDLINE);
    }

    private int runMenu(){
        if(isMenuPrint){
            menu.printDefaultMenu();
        }
        System.out.print(PrintTemplate.MENU_PROMPT);
        isMenuPrint = true;
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
            System.out.println(PrintTemplate.BOLDLINE);
            isMenuPrint = false;
        }


        if(cmdCode == EXITCODE){
            if(isPlaying){
                showSaveAndList();
            }else{
                System.out.print(PrintTemplate.MENU_PROMPT);
            }

            System.out.print(Command.YES_OR_NO_EXIT);
            boolean input = MenuInput.yesOrNoInput();
            if(input){
                menu.printWithTemplate(Command.EXIT.toString());
                System.exit(0);
            }
        }


        if(cmdCode == STARTCODE){
            if(!isPlaying){
                playerTurn = PieceColor.WHITE;
                isPlaying = true;
                isGamePrint = true;
                board = new Board();
            }
            //isPlaying = true의 경우는 gameInput에서 처리 됨
        }

        if(cmdCode == QUITCODE){
            if(isPlaying){
                showSaveAndList();
                System.out.print(Command.YES_OR_NO_QUIT);
                boolean input = MenuInput.yesOrNoInput();
                if(input){
                    menu.printWithTemplate(Command.QUIT.toString());
                    isPlaying = false;
                }
            }
            isMenuPrint = true;

        }

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
                System.out.println(PrintTemplate.BOLDLINE);
            }else{
                System.out.println(PrintTemplate.BOLDLINE);
                System.out.println(FileError.FAILED_SAVE);
                System.out.println(PrintTemplate.BOLDLINE);
            }
            isMenuPrint = false;

        }

        if(cmdCode == LOADCODE) {
            int slot;
            if(isPlaying){
                showSaveAndList();
                System.out.print(Command.YES_OR_NO_LOAD);
                boolean input = MenuInput.yesOrNoInput();
                if(!input){ return; }
                slot = GameInput.number;
            }else{
                slot = MenuInput.number;
            }
            board = new Board();
            int isLoad = (fileManager.loadSavedFile(slot, board));
            if (isLoad == 1) {
                playerTurn = board.getCurrentTurn();
                isPlaying = true;
                isGamePrint = true;
                System.out.println(PrintTemplate.BOLDLINE);
                System.out.println(FileMessage.SAVE_LOADED.format(slot));
                System.out.println((FileMessage.SAVE_NAME.format(slot,fileManager.getFilename().get(slot-1))));
                System.out.println(PrintTemplate.BOLDLINE + "\n");
            }else if(isLoad == 0){
                System.out.println(PrintTemplate.BOLDLINE);
                System.out.println(FileError.FAILED_LOAD.format(slot));
                System.out.println(PrintTemplate.BOLDLINE);
                isMenuPrint = false;
                isGamePrint = false;
            }else{
                System.out.println(PrintTemplate.BOLDLINE);
                System.out.println(FileError.FAILED_LOAD);
                isMenuPrint = false;
                isGamePrint = false;
            }
        }

        if (cmdCode == DELSAVECODE) {
            if(!isPlaying){
                filePrint.deleteFilePrint(MenuInput.number);
                isMenuPrint = false;
            }else{
                System.out.println(CommandError.DELSAVE_BLOCK);
            }
        }

        if (cmdCode == SAVEFILECODE) {
            filePrint.saveListPrint();
            isMenuPrint = false;
        }

    }
}
