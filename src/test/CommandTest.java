package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import data.Command;

public class CommandTest {

    @DisplayName("Command Message Test")
    @Test
    void commandMessageTest(){
        System.out.println(Command.HELP);
        System.out.println(Command.EXIT);
        System.out.println(Command.START);
        System.out.println(Command.QUIT);
        System.out.println(Command.YES_OR_NO_WHILE_NOT_SAVED);


        try{
            System.out.println(Command.SAVE);
            System.out.println(Command.DELSAVE);
            System.out.println(Command.SAVEFILE);
            System.out.println(Command.LOAD);
//            System.out.println(Command.SAVE.formatStr(6));
//            System.out.println(Command.SAVEFILE.formatStr("ada", "fdasdf"));
//            System.out.println(Command.LOAD.formatStr("1", "2", 3));

        }catch(Exception e){
            if(e instanceof IllegalArgumentException){
                Assertions.assertEquals("This command must have arguments", e.getMessage());
            }
        }

        System.out.println(Command.SAVE.formatStr(3));
        System.out.println(Command.DELSAVE.formatStr(3));
        System.out.println(Command.SAVEFILE.formatStr("Hello", "Hello", "World!", "Hi", "fadsf"));
        System.out.println(Command.LOAD.formatStr(1, 1, "Hello"));

    }
}
