package gameManager;

import board.Chaturanga;
import board.ThreeCheckBoard;
import check.Checker;
import check.GameEnd;
import data.*;

import Menu.Menu;
import fileManager.*;
import User.*;
import board.Board;
import board.PawnGameBoard;

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
    private final int REGISTERCODE = GameInputReturn.REGISTER.getCode();
    private final int LOGINCODE =  GameInputReturn.LOGIN.getCode();
    private final int LOGOUTCODE =  GameInputReturn.LOGOUT.getCode();
    private final int TOGGLECODE =   GameInputReturn.TOGGLE.getCode();
    private final int OPTIONCODE =    GameInputReturn.OPTION.getCode();
    private final int COORDINATECODE = GameInputReturn.COORDINATE_TRUE.getCode();

    public static String USER_ID = PrintTemplate.GUEST.toString();


    public boolean isPlaying = false;
    public boolean isRunning = true;
    public boolean isSaved = false;
    public boolean isLogin = false;

    private boolean isMenuPrint = true;
    private boolean isGamePrint = true;
    private PieceColor playerTurn;


    private FileManager fileManager;
    private FilePrint filePrint;
    private UserManager userManager;
    private final Menu menu;
    private Board board;

    public boolean canEnpassant=true, canCastling=true, canPromotion=true ;

    public GameManager() {
        fileManager = FileManager.getInstance();
        filePrint = new FilePrint(fileManager);
        userManager = new UserManager(fileManager);
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
            if(cmdCode >= HELPCODE && cmdCode <= OPTIONCODE){
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
                if(isPlaying) { isGameEnd(PieceColor.WHITE); }
                //턴 전환
                if(isPlaying){
                    board.turnChange();
                    playerTurn = board.getCurrentTurn();
                    Checker checker = new Checker(playerTurn);
                    boolean isCheck = checker.isCheck(board);
                    if(isCheck) {
                        if (!(board instanceof ThreeCheckBoard)) {
                            if (playerTurn == PieceColor.BLACK) {
                                System.out.println(PrintTemplate.BOLDLINE);
                                System.out.println(PrintTemplate.CHECK_BLACK);
                                System.out.println(PrintTemplate.BOLDLINE);
                            } else {
                                System.out.println(PrintTemplate.BOLDLINE);
                                System.out.println(PrintTemplate.CHECK_WHITE);
                                System.out.println(PrintTemplate.BOLDLINE);
                            }
                        }
                    }
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
        boolean isEnd = false;

        //1. CheckMate
        if(gameEnd.isCheckMate(board)){
            System.out.println(PrintTemplate.BOLDLINE);
            if(pieceColor == PieceColor.WHITE) { System.out.println(PrintTemplate.END_BLACK_CHECKMATE); }
            else { System.out.println(PrintTemplate.END_WHITE_CHECKMATE); }
            System.out.println(PrintTemplate.BOLDLINE + "\n");
            isEnd = true;
        }

        if(!isEnd && pieceColor != board.getCurrentTurn()){
            if(gameEnd.isStaleMate(board)){
                if(board instanceof Chaturanga){
                    System.out.println(PrintTemplate.BOLDLINE);
                    if(board.getCurrentTurn() == PieceColor.WHITE){
                        System.out.println(PrintTemplate.END_WHITE_STALEMATE);
                    }else{
                        System.out.println(PrintTemplate.END_BLACK_STALEMATE);
                    }
                }else{
                    System.out.println(PrintTemplate.BOLDLINE);
                    System.out.println(PrintTemplate.END_STALEMATE);
                    System.out.println(PrintTemplate.BOLDLINE + "\n");
                }
                isEnd = true;
            }
        }

        if(!isEnd && gameEnd.isInsufficientPieces(board)){
            System.out.println(PrintTemplate.BOLDLINE);
            System.out.println(PrintTemplate.END_INSUFFICIENT);
            System.out.println(PrintTemplate.BOLDLINE + "\n");
            isEnd = true;
        }

        if(!isEnd && board instanceof ThreeCheckBoard){
            Checker checker = new Checker(pieceColor);
            if(checker.isCheck(board)){
                ThreeCheckBoard tempBoard = (ThreeCheckBoard) board;
                if(pieceColor == PieceColor.WHITE){
                    tempBoard.ThreeCheckW++;
                    if(tempBoard.ThreeCheckW < 3){
                        System.out.println(PrintTemplate.BOLDLINE);
                        System.out.println(PrintTemplate.CHECK_WHITE + PrintTemplate.COUNT.formatMessage(tempBoard.ThreeCheckW));
                        System.out.println(PrintTemplate.BOLDLINE);
                    }
                }else{
                    tempBoard.ThreeCheckB++;
                    if(tempBoard.ThreeCheckB < 3){
                        System.out.println(PrintTemplate.BOLDLINE);
                        System.out.println(PrintTemplate.CHECK_BLACK + PrintTemplate.COUNT.formatMessage(tempBoard.ThreeCheckB));
                        System.out.println(PrintTemplate.BOLDLINE);
                    }
                }

                if(tempBoard.ThreeCheckB >= 3){
                    System.out.println(PrintTemplate.BOLDLINE);
                    System.out.println(PrintTemplate.THREE_CHECK_WHITE);
                    System.out.println(PrintTemplate.BOLDLINE);
                    isEnd = true;
                }else if(tempBoard.ThreeCheckW >= 3){
                    System.out.println(PrintTemplate.BOLDLINE);
                    System.out.println(PrintTemplate.THREE_CHECK_BLACK);
                    System.out.println(PrintTemplate.BOLDLINE);
                    isEnd = true;
                }
            }
        }

        if(isEnd){
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
            int num;
            if(isPlaying){ num = GameInput.number; }
            else num = MenuInput.number;
            System.out.println(PrintTemplate.BOLDLINE);

            switch (num){
                case 1 -> System.out.println(Command.HELP1);
                case 2 -> System.out.println(Command.HELP2);
                case 3 -> System.out.println(Command.HELP3);
                case 4 -> System.out.println(Command.HELP4);
                default -> throw new IllegalArgumentException("Invalid command");
            }
            System.out.println(PrintTemplate.BOLDLINE);
            isMenuPrint = false;

            return;
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

            return;
        }


        if(cmdCode == STARTCODE){
            if(!isPlaying){
                playerTurn = PieceColor.WHITE;
                isPlaying = true;
                isSaved = false;
                isGamePrint = true;
                int num = MenuInput.number;
                switch(num){
                    case 1 -> board = new Board(canEnpassant, canCastling, canPromotion, true);
                    case 2 -> board = new ThreeCheckBoard(true, true, true, true);
                    case 3 -> board = new Chaturanga(false, false, true, true);
                    case 4 -> board = new PawnGameBoard(true);
                }

            }else{
                System.out.println(PrintTemplate.BOLDLINE);
                System.out.println(CommandError.START_BLOCK);
                System.out.println(PrintTemplate.BOLDLINE);
                isGamePrint = false;
            }

            return;
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

            return;
        }

        if(cmdCode == SAVECODE){
            int slot;
            if(!isPlaying){
                board = new Board(canEnpassant, canCastling, canPromotion, true);
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

            return;
        }

        if(cmdCode == LOADCODE) {
            int slot;
            if(isPlaying){ slot = GameInput.number; }
            else{ slot = MenuInput.number; }

            Board tempBoard = null;

            boolean isEmptySlot = fileManager.isEmptySlot(slot);

            if(!isEmptySlot){
                tempBoard = fileManager.loadSavedFile(slot);
            }else{
                System.out.println(PrintTemplate.BOLDLINE);
                System.out.println(FileError.FAILED_LOAD.format(slot));
                System.out.println(PrintTemplate.BOLDLINE);
                isMenuPrint = false;
                isGamePrint = false;
                return;
            }

            if (tempBoard != null) {
                if(isPlaying){
                    showSaveAndList();
                    System.out.print(Command.YES_OR_NO_LOAD);
                    boolean input = MenuInput.yesOrNoInput();
                    if(!input){ return; }
                }
                board = tempBoard;
                playerTurn = board.getCurrentTurn();
                isPlaying = true;
                isGamePrint = true;
                isSaved = true;
                System.out.println(PrintTemplate.BOLDLINE);
                System.out.println(FileMessage.SAVE_LOADED.format(slot));
                System.out.println((FileMessage.SAVE_NAME.format(slot,fileManager.getFilename().get(slot-1))));
                System.out.println(PrintTemplate.BOLDLINE + "\n");
            }else{
                System.out.println(PrintTemplate.BOLDLINE);
                System.out.println(FileError.FAILED_LOAD_ER);
                System.out.println(PrintTemplate.BOLDLINE);
                isMenuPrint = false;
                isGamePrint = false;
            }

            return;
        }

        if (cmdCode == DELSAVECODE) {
            if(!isPlaying){
                filePrint.deleteFilePrint(MenuInput.number);
                isMenuPrint = false;
            }else{
                System.out.println(PrintTemplate.BOLDLINE);
                System.out.println(CommandError.CMD_BLOCK);
                System.out.println(PrintTemplate.BOLDLINE);
                isGamePrint = false;
            }

            return;
        }

        if (cmdCode == SAVEFILECODE) {
            filePrint.saveListPrint();
            isMenuPrint = false;

            return;
        }

        if (cmdCode == REGISTERCODE){
            if(isPlaying){
                System.out.println(PrintTemplate.BOLDLINE);
                System.out.println(CommandError.CMD_BLOCK);
                System.out.println(PrintTemplate.BOLDLINE);

                return;
            }
            System.out.println(PrintTemplate.BOLDLINE);
            System.out.println(Command.REGISTER_START);
            System.out.println(PrintTemplate.BOLDLINE);


            //id 입력
            String idStr = null;
            while(true){
                if(MenuInput.accountInput(true)) {
                    idStr = MenuInput.idStr;
                    if (!(userManager.isDuplicate(idStr)))
                        break;
                    else {
                        System.out.println(PrintTemplate.BOLDLINE);
                        System.out.println(CommandError.ID_EXISTING.formatMessage(idStr));
                        System.out.println(PrintTemplate.BOLDLINE);
                    }
                }else{
                    System.out.println(PrintTemplate.BOLDLINE);
                    System.out.println(Command.ACC_INPUT_TERMINATE);
                    System.out.println(PrintTemplate.BOLDLINE);
                    return;
                }
            }

            //false인 경우는 프로세스 종료
            if (MenuInput.accountInput(false)) {
                String pwStr = MenuInput.pwStr;
                if (userManager.registerUser(idStr, pwStr)) {
                    System.out.println(PrintTemplate.BOLDLINE);
                    System.out.println(Command.REGISTER_SUCCESS.formatStr(idStr));
                    System.out.println(PrintTemplate.BOLDLINE);
                }
            }else{
                System.out.println(PrintTemplate.BOLDLINE);
                System.out.println(Command.ACC_INPUT_TERMINATE);
                System.out.println(PrintTemplate.BOLDLINE);
                return;
            }

            return;
        }

        if (cmdCode == LOGINCODE){
            if(isPlaying){
                System.out.println(PrintTemplate.BOLDLINE);
                System.out.println(CommandError.CMD_BLOCK);
                System.out.println(PrintTemplate.BOLDLINE);

                return;
            }
            if(isLogin){
                System.out.println(PrintTemplate.BOLDLINE);
                System.out.println(CommandError.LOGIN_FAIL);
                System.out.println(PrintTemplate.BOLDLINE);

                return;
            }

            String idStr = null;
            while(true){
                if(MenuInput.accountInput(true)){
                    idStr = MenuInput.idStr;
                    if(userManager.isDuplicate(idStr)) { break; }
                    else {
                        System.out.println(PrintTemplate.BOLDLINE);
                        System.out.println(CommandError.ID_INVALID.formatMessage(idStr));
                        System.out.println(PrintTemplate.BOLDLINE);

                    }
                }else{
                    System.out.println(PrintTemplate.BOLDLINE);
                    System.out.println(Command.ACC_INPUT_TERMINATE);
                    System.out.println(PrintTemplate.BOLDLINE);
                    return;
                }
            }

            while(true) {
                if (MenuInput.accountInput(false)) {
                    String pwStr = MenuInput.pwStr;
                    if (userManager.loginUser(idStr, pwStr)) {
                        System.out.println(PrintTemplate.BOLDLINE);
                        System.out.println(Command.LOGIN_SUCCESS.formatStr(idStr));
                        System.out.println(PrintTemplate.BOLDLINE);

                        //로그인
                        isLogin = true;
                        USER_ID = idStr;
                        break;
                    } else {
                        System.out.println(PrintTemplate.BOLDLINE);
                        System.out.println(CommandError.PW_UNMATCH);
                        System.out.println(PrintTemplate.BOLDLINE);
                    }
                }else{
                    System.out.println(PrintTemplate.BOLDLINE);
                    System.out.println(Command.ACC_INPUT_TERMINATE);
                    System.out.println(PrintTemplate.BOLDLINE);
                    return;
                }
            }
            return;
        }

        if (cmdCode == LOGOUTCODE){
            if(isPlaying){
                System.out.println(PrintTemplate.BOLDLINE);
                System.out.println(CommandError.CMD_BLOCK);
                System.out.println(PrintTemplate.BOLDLINE);

                return;
            }

            if(isLogin){
                System.out.println(PrintTemplate.BOLDLINE);
                System.out.println(Command.LOGOUT.formatStr(USER_ID));
                System.out.println(PrintTemplate.BOLDLINE);
                USER_ID = PrintTemplate.GUEST.toString();
                isLogin = false;
            }else{
                System.out.println(PrintTemplate.BOLDLINE);
                System.out.println(CommandError.LOGOUT_FAIL);
                System.out.println(PrintTemplate.BOLDLINE);
            }
            return;
        }

        if (cmdCode == TOGGLECODE){
            if(isPlaying){
                System.out.println(PrintTemplate.BOLDLINE);
                System.out.println(CommandError.CMD_BLOCK);
                System.out.println(PrintTemplate.BOLDLINE);

                return;
            }
            int num = MenuInput.toggleNum;

            System.out.println(PrintTemplate.BOLDLINE);

            if(MenuInput.toggleOn){
                switch (num){
                    case 0 -> canEnpassant = true;
                    case 1 -> canCastling = true;
                    case 2 -> canPromotion = true;
                }

                System.out.println(Command.TOGGLE_ON.formatStr(num));
            }else{
                switch (num){
                    case 0 -> canEnpassant = false;
                    case 1 -> canCastling = false;
                    case 2 -> canPromotion = false;
                }

                System.out.println(Command.TOGGLE_OFF.formatStr(num));
            }


            System.out.println(PrintTemplate.BOLDLINE);
        }

        if(cmdCode == OPTIONCODE){
            System.out.println(PrintTemplate.BOLDLINE);
            System.out.println(Command.OPTION.formatStr(canPromotion, canEnpassant, canCastling));
            System.out.println(PrintTemplate.BOLDLINE);
            isMenuPrint = false;
            return;
        }

    }
}