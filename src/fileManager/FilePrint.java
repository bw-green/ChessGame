package fileManager;

import data.FileMessage;
import data.FileError;
import data.PrintTemplate;
import board.Board;
import gameManager.GameManager;

import java.util.ArrayList;

public class FilePrint {
    private final FileManager fileManager;

    public FilePrint(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void saveListPrint() { //./savefile 명령어 호출시
        System.out.println(PrintTemplate.BOLDLINE);
        System.out.println(PrintTemplate.CURRENT_USER + GameManager.USER_ID);
        showFileList();
        System.out.println(PrintTemplate.MENU_LAST_SAVE);
        System.out.println(PrintTemplate.BOLDLINE);
    }

    public void saveFilePrint(int slot, Board board) { // ./save 명령어 호출시
        System.out.println(PrintTemplate.BOLDLINE);
        if(fileManager.overWriteSavedFile(slot, board)) {
            System.out.println(PrintTemplate.CURRENT_USER + GameManager.USER_ID);
            showFileList();
            System.out.println(FileMessage.SAVE_CREATED.format(slot));
        }
        else System.out.println(FileError.FAILED_SAVE);
        System.out.println(PrintTemplate.BOLDLINE);
    }
    public void deleteFilePrint(int slot) { // ./delsave 명령어 호출시
        System.out.println(PrintTemplate.BOLDLINE);
        if(fileManager.deleteSavedFile(slot)) {
            System.out.println(PrintTemplate.CURRENT_USER + GameManager.USER_ID);
            showFileList();
            System.out.println(FileMessage.SAVE_DELETED.format(slot));
        }
        else System.out.println(FileError.FAILED_DELETE);
        System.out.println(PrintTemplate.BOLDLINE);
    }

    public void loadFilePrint(int slot) {
        System.out.println(PrintTemplate.BOLDLINE);
        if(fileManager.loadSavedFile(slot) != null) {
            System.out.println(FileMessage.SAVE_LOADED.format(slot));
            System.out.println((FileMessage.SAVE_NAME.format(slot,fileManager.getFilename().get(slot-1))));
        }
        else if(fileManager.loadSavedFile(slot)== null) System.out.println(FileError.FAILED_LOAD_ER);
        else System.out.println(FileError.FAILED_LOAD.format(slot));
        System.out.println(PrintTemplate.BOLDLINE);
    }

    public void showFileList() {

        ArrayList<String> filename = fileManager.getFilename();
        String LSFile = fileManager.getLastSavedFile();
        String LSFileNum = Integer.toString(fileManager.getLastSaveFileNum()+1); //값 0~4에서 1~5로 맞춰주기
        if(LSFileNum.equals("0")){LSFileNum = "No";} //기획서에 맞게 LSF가 없을경우, No로 출력해주기

        System.out.println(FileMessage.LSF_LINE);
        System.out.println(FileMessage.LSF_NAME.format(LSFileNum,LSFile));
        System.out.println(PrintTemplate.INTERLINE);
        for (int i = 0; i < filename.size(); i++) {
            System.out.println(FileMessage.SAVE_NAME.format((i+1),filename.get(i)));
        }
        System.out.println(PrintTemplate.INTERLINE);
    }
}
