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
            "e2 e2",   //문법상의 success -> board에서 의미규칙상에서 유효성 처리
            "e1 e5                  ",
            "e2              e4",
            "E2 E4",
            "'e2\te4'"
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
            "e23 e4",  // board에서 벗어난 좌표
            "x3 a3", // board에서 벗어난 좌표
            "'  '", // 여러 칸 공백
            "''",    // 진짜 빈 문자열
            "'e2$ e4'",
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
