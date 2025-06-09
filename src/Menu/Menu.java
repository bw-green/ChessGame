package Menu;

import fileManager.FilePrint;
import data.PrintTemplate;
import gameManager.GameManager;


public class Menu {
    private FilePrint filePrint;
    public Menu(FilePrint filePrint) {
        this.filePrint = filePrint;
    }

    public void printDefaultMenu(){
        System.out.println(PrintTemplate.MENULINE);
        System.out.println(PrintTemplate.CURRENT_USER + GameManager.USER_ID);
        filePrint.showFileList();
        System.out.println(PrintTemplate.MENU_FIRST_INSTRUCT);
        System.out.println(PrintTemplate.BOLDLINE);
    }

    public void printWithTemplate(String str){
        System.out.println(PrintTemplate.BOLDLINE);
        System.out.println(str);
        System.out.println(PrintTemplate.BOLDLINE);
    }

}
