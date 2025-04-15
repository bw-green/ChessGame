package fileManager;

import data.PrintTemplate;

import java.util.ArrayList;

public class FilePrint {
    private final FileManager fileManager;

    public FilePrint(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void saveListPrint() { //./savefile 명령어 호출시
        System.out.println(PrintTemplate.BOLDLINE);
        showFileList();
        System.out.println(PrintTemplate.MENU_LAST_SAVE);
        System.out.println(PrintTemplate.BOLDLINE);
    }

    public void saveFilePrint(int slot) {
        System.out.println(PrintTemplate.BOLDLINE);
        if(fileManager.overWriteSavedFile(slot)) {
            showFileList();
            System.out.println("| The save "+slot+" has been created.|");
        }
        else System.out.println("| Failed to save the game. |");
        System.out.println(PrintTemplate.BOLDLINE);
    }
    public void deleteFilePrint(int slot) {
        System.out.println(PrintTemplate.BOLDLINE);
        if(fileManager.deleteSavedFile(slot)) {
            showFileList();
            System.out.println("| The save "+slot+" has deleted |");
        }
        else System.out.println("| Failed to delete the save file |");
        System.out.println(PrintTemplate.BOLDLINE);
    }

    public void loadFilePrint(int slot) {
        System.out.println(PrintTemplate.BOLDLINE);
        if(fileManager.loadSavedFile(slot)) {
            System.out.println("| The save "+slot+" has loaded |");
            System.out.println("|save "+slot+".|< "+fileManager.getFilename().get(slot-1)+" >");
        }
        else System.out.println("| Failed to load the savefile. |");
        System.out.println(PrintTemplate.BOLDLINE);
    }

    public void showFileList() {

        ArrayList<String> filename = fileManager.getFilename();
        String LSFile = fileManager.getLastSavedFile();
        int LSFileNum = fileManager.getLastSaveFileNum();

        System.out.println("-----------------<Last Save File>-----------------");
        System.out.println("|save " + (LSFileNum+1) + ".| " + LSFile);
        System.out.println(PrintTemplate.INTERLINE);
        for (int i = 0; i < filename.size(); i++) {
            System.out.println("|save " + (i+1) + ".|<" + filename.get(i) + ">");
        }
        System.out.println(PrintTemplate.INTERLINE);
    }
}
