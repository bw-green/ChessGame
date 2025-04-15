package gameManager;

import data.Command;
import data.FileError;
import Menu.Menu;

import fileManager.FileManager;


public class GameManager {

    public boolean isSaved = false;
    public boolean isPlaying = false;
    public boolean isExit = false;

    private FileManager fileManager;
    private Menu menu;
    private int cmdCode = 0;

    private int runProgram(){
        if(cmdCode == 2){
            if(isPlaying){
                //게임 진행중일 때
            }
        }
        if(cmdCode == 3){

        }
        if(cmdCode == 6){

        }

        return 0;
    }


    private int runMenu(){
        cmdCode = menu.runMenu();


        return cmdCode;
    }

    private int runGame(){

        return 0;
    }


}
