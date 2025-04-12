package test.java;

import data.COMMAND_ERROR;
import data.FILE_ERROR;
import data.INVALID_COORDINATE;
import data.UNSPECIFIED;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ErrorTest { // 에러 코드 나오게하기

    @DisplayName("Error Parameter Test")
    @Test
    void ErrorParameterTest() {
        try{
            System.out.println(COMMAND_ERROR.EMPTY_SLOT.formatMessage(6));
            System.out.println(COMMAND_ERROR.EMPTY_SLOT);
        }catch(Exception e){
            if(e instanceof NumberFormatException){
                Assertions.assertEquals("slot index out of bounds", e.getMessage());
            }
            if(e instanceof NullPointerException){
                Assertions.assertEquals("slot argument is empty", e.getMessage());
            }
        }

        System.out.println(COMMAND_ERROR.EMPTY_SLOT.formatMessage(3));
        System.out.println(INVALID_COORDINATE.INVALID_MOVE);
        System.out.println(COMMAND_ERROR.START_BLOCK);
        System.out.println(FILE_ERROR.FAILED_DELETE);
        System.out.println(UNSPECIFIED.CASTLING_FAILED);
    }


}
