package Menu;

import data.Command;
import data.PrintTemplate;
import Menu.MenuInput;
import data.GameInputReturn;
import fileManager.FileManager;
import fileManager.FilePrint;


public class Menu {
    private FilePrint filePrint;
    public Menu() {

    }

    public void printDefaultMenu(){
        System.out.println(PrintTemplate.BOLDLINE);
        //filemanager로 savefile 출력]
        filePrint.showFileList();
        System.out.println(PrintTemplate.GAME_BASE_INSTRUCT);
        System.out.println(PrintTemplate.BOLDLINE);
        System.out.println(PrintTemplate.MENU_PROMPT);
    }

    public void printWithTemplate(String str){
        System.out.println(PrintTemplate.BOLDLINE);
        System.out.println(str);
        System.out.println(PrintTemplate.BOLDLINE);
    }

}
