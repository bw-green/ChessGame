package test.java.Input;


import data.GameInputReturn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import userinput.UserInput;


public class UserInputTest {



    @ParameterizedTest
    @CsvSource({
            "e2 f2",
            "b1 c3  ",
            "e2 e4",
            "e1 e5                  ",
            "e2              e4"
    })
    @DisplayName("input_success")
    void InputTrueTest(String input) {
        Assertions.assertEquals(GameInputReturn.COORDINATE_TRUE.getCode(), UserInput.handleInput(input));
        System.out.println(UserInput.toStringMove());
    }


    @ParameterizedTest
    @CsvSource({
            "e 3 f2",
            "e2",
            "e2f2",
            "c8 e3 f2",
            "'  '", // 여러 칸 공백
            "''",    // 진짜 빈 문자열
            "null"
    })
    @DisplayName("input_fail")
    void InputFalseTest(String input) {
        Assertions.assertEquals(GameInputReturn.ERROR.getCode(), UserInput.handleInput(input));
    }

    @DisplayName("null input 처리")
    @Test
    void nullInputTest() {
        Assertions.assertEquals(GameInputReturn.ERROR.getCode(), UserInput.handleInput(null));
    }

}
