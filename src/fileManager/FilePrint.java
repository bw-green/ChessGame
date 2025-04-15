package fileManager;

import java.util.ArrayList;

public class FilePrint {
    FileManager fileManager;
    public FilePrint(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void saveListPrint() {
        showFileList();
        System.out.println("|the last save file and the list of save files");
    }

    public void saveFilePrint(int slot) {
        if(fileManager.overWriteSavedFile(slot)) {
            showFileList();
            System.out.println("| The save "+slot+" has been created.|");
        }
        else System.out.println("| Failed to save the game. |");
    }
    public void deleteFilePrint(int slot) {
        if(fileManager.deleteSavedFile(slot)) {
            showFileList();
            System.out.println("| The save "+slot+" has deleted |");
        }
        else System.out.println("| Failed to delete the save file |");
    }

    public void loadFilePrint(int slot) {
        if(fileManager.loadSavedFile(slot)) {
            System.out.println("| The save "+slot+" has loaded |");
            System.out.println("|save "+slot+".|< "+fileManager.getFilename().get(slot-1)+" >");
        }
        else System.out.println("| Failed to load the savefile. |");
    }

    private void showFileList() {
        ArrayList<String> filename = fileManager.getFilename();
        String LSFile = fileManager.getLastSavedFile();
        int LSFileNum = fileManager.getLastSaveFileNum();

        System.out.println("-----------------<Last Save File>-----------------");
        System.out.println("|save " + LSFileNum + ".| " + LSFile);
        System.out.println("--------------------------------------------------");
        for (int i = 0; i < filename.size(); i++) {
            System.out.println("|save " + (i+1) + ".|<" + filename.get(i) + ">");
        }
        System.out.println("--------------------------------------------------");
    }
}
