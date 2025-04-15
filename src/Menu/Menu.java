package Menu;

import data.Command;
import data.FileError;
import data.PrintTemplate;
import Menu.MenuInput;
import data.GameInputReturn;
import fileManager.FileManager;

import java.rmi.UnexpectedException;

public class Menu {
    private final int ERRORCODE = GameInputReturn.ERROR.getCode();
    private final int HELPCODE = GameInputReturn.HELP.getCode();
    private final int EXITCODE = GameInputReturn.EXIT.getCode();
    private final int STARTCODE = GameInputReturn.START.getCode();
    private final int QUITCODE = GameInputReturn.QUIT.getCode();
    private final int SAVECODE = GameInputReturn.SAVE.getCode();
    private final int LOADCODE = GameInputReturn.LOAD.getCode();
    private final int DEL_SAVECODE = GameInputReturn.DEL_SAVE.getCode();
    private final int SAVE_FILECODE = GameInputReturn.SAVE_FILE.getCode();

    //FileManager 객체를 어디서 부터 만들어서 갖고 있을지 생각해야됨. + 복사 수준도 고려
    private FileManager fileManager = new FileManager();
    private int cmdCode;
    private String menuStr;
    private boolean isMenu = true;

    public int runMenu(){
        printDefaultMenu();
        while(isMenu){
            getCommand();
            cmdBeforePrint();
            cmdAfterPrint();

            //getCommand
            //runCommand 2개로 끝내기
        }

        //EXIT, START, LOAD 일때만 메뉴를 종료.
        if(cmdCode == 2 || cmdCode == 3 || cmdCode == 6)
            return cmdCode;

        //의도하지 않은 메뉴 종료
        throw new RuntimeException("Not intended way to exit menu");
    }

    private int getCommand(){
        cmdCode = 0;
        cmdCode = MenuInput.menuInput();

        return 0;
    }

    private int printWithTemplate(String str){

        System.out.println(PrintTemplate.BOLDLINE);
        System.out.println(str);
        System.out.println(PrintTemplate.BOLDLINE);
        return 0;
    }


    private int cmdBeforePrint(){
        switch (cmdCode){
            case 1 ->{ //HELP
                printWithTemplate(Command.HELP.toString());
            }
            case 2 ->{ //EXIT
                boolean input = MenuInput.yesornoInput();
                if(input){
                    isMenu = false;
                    printWithTemplate(Command.EXIT.toString());
                }else{
                    printDefaultMenu();
                }
            }
            case 3 ->{ //START
                //출력 X
            }
            case 4 ->{ //QUIT
                //출력 X
                printDefaultMenu();
            }
            case 5 ->{ //SAVE
                //filemanager에서 실행
                boolean isSaved = fileManager.overWriteSavedFile(MenuInput.number);
                if(isSaved){
                    menuStr = Command.SAVE.formatStr(MenuInput.number);
                }else{
                    menuStr = FileError.FAILED_SAVE.toString();
                }
                isMenu = false;
            }
            case 6 ->{ //LOAD
                //filemanager에서 실행
//                boolean isLoad = fileManager.loadSavedFile(MenuInput.number);
//                if(isLoad){
//                    menuStr = Command.LOAD.formatStr(MenuInput.number, MenuInput.number, "");
//                }else{
//                    menuStr = FileError.FAILED_LOAD.toString();
//                }
                isMenu = false;
            }
            case 7 ->{ //DELSAVE
                //filemanager에서 실행
//                boolean isDelete = fileManager.deleteSavedFile(MenuInput.number);
//                if(isDelete){
//                    menuStr = Command.DELSAVE.formatStr(MenuInput.number);
//                }else{
//                    menuStr = FileError.FAILED_DELETE.toString();
//                }
            }
            case 8 ->{ //SAVEFILE
//                //filemanager에서 실행
//                String[] savefileStr = new String[5];
//                //filemanager 호출 -> savefile 이름 받아와야됨
//                menuStr = Command.SAVEFILE.formatStr(savefileStr);
            }
            case 0 -> {
                //에러는 이미 MenuInput에서 출력. 다시 돌아가는 용도.
            }
            default ->{

            }
        }
        return 0;
    }

    private int printDefaultMenu(){
        System.out.println(PrintTemplate.BOLDLINE);
        //filemanager로 savefile 출력
        System.out.println(PrintTemplate.GAME_BASE_INSTRUCT);
        System.out.println(PrintTemplate.BOLDLINE);
        System.out.println(PrintTemplate.MENU_PROMPT);

        return 0;
    }

    private int cmdAfterPrint(){
        switch (cmdCode){
            case 0, 1, 7, 8 -> {

            }

            case 2 -> {
                boolean input = MenuInput.yesornoInput();
                if(input){
                    isMenu = false;
                    printWithTemplate(Command.EXIT.toString());
                }else{
                    printDefaultMenu();
                }

            }

            case 3 -> {
                isMenu = false;

            }

            case 4 -> {
                isMenu = false;

            }

            case 5 -> {

            }

            case 6 -> {

            }

        }
        return 0;
    }

}
