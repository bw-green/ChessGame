package Menu;

import fileManager.FilePrint;
import data.PrintTemplate;



public class Menu {
    private FilePrint filePrint;
    public Menu(FilePrint filePrint) {
        this.filePrint = filePrint;
    }

    public void printDefaultMenu(){
        System.out.println(PrintTemplate.MENULINE);
        //filemanager로 savefile 출력
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
