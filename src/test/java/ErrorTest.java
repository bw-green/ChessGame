package test.java;

import data.CommandError;
import data.FileError;
import data.InvalidCoordinate;
import data.Unspecified;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ErrorTest { // 에러 코드 나오게하기

    @DisplayName("Error Parameter Test")
    @Test
    void ErrorParameterTest() {
        try{
            System.out.println(CommandError.EMPTY_SLOT.formatMessage(6));
            System.out.println(CommandError.EMPTY_SLOT);
        }catch(Exception e){
            if(e instanceof NumberFormatException){
                Assertions.assertEquals("slot index out of bounds", e.getMessage());
            }
            if(e instanceof NullPointerException){
                Assertions.assertEquals("slot argument is empty", e.getMessage());
            }
        }

        System.out.println(CommandError.EMPTY_SLOT.formatMessage(3));
        System.out.println(InvalidCoordinate.INVALID_MOVE);
        System.out.println(CommandError.START_BLOCK);
        System.out.println(FileError.FAILED_DELETE);
        System.out.println(Unspecified.CASTLING_FAILED);
    }


}
