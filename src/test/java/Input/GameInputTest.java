package test.java.Input;

import Input.GameInput;
import data.GameInputReturn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;

import java.io.ByteArrayInputStream;

public class GameInputTest {

    @ParameterizedTest
    @NullSource
    @DisplayName("널 값 테스트")
    void testWithNull(String input) {
        System.setIn(new ByteArrayInputStream("\n".getBytes()));
        Assertions.assertEquals(-1, GameInput.gameInput());
    }

    @ParameterizedTest
    @CsvSource({
            "e2 e4",
            "e3 e6"
    })
    @DisplayName("다른 input에서 처리")
    void NotMIne(String input) {
        //when
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Assertions.assertEquals(-1, GameInput.gameInput());
    }

    @ParameterizedTest
    @CsvSource({
            "/",
            "/halp",
            "/ help",
            "/  help",
            "/ help  3",
            "/help2",
            "/help 3"
    })
    @DisplayName("인자 없는 오류 명령어들")
    void ErrorNoNumberTest(String input) {
        //when
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Assertions.assertEquals(0, GameInput.gameInput());
    }

    @ParameterizedTest
    @CsvSource({
            "/help",
            "/help   ",
            "/help          "
    })
    @DisplayName("help명령어")
    void helpTest(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Assertions.assertEquals(GameInputReturn.HELP.getCode(), GameInput.gameInput());
    }

    @ParameterizedTest
    @CsvSource({
            "/exit",
            "/exit   ",
            "/exit          "
    })
    @DisplayName("exit명령어")
    void exitTest(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Assertions.assertEquals(GameInputReturn.EXIT.getCode(), GameInput.gameInput());
    }

    @ParameterizedTest
    @CsvSource({
            "/savefile 2",
            "/savefile 3  ",
            "/savefile 4        "
    })
    @DisplayName("savefile명령어")
    void saveFileTest(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Assertions.assertEquals(GameInputReturn.SAVE_FILE.getCode(), GameInput.gameInput());
        System.out.println(GameInput.number);
    }

    @ParameterizedTest
    @CsvSource({
            "/start",
            "/start   ",
            "/start          "
    })
    @DisplayName("start명령어")
    void startTest(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Assertions.assertEquals(GameInputReturn.START.getCode(), GameInput.gameInput());
    }

    @ParameterizedTest
    @CsvSource({
            "/quit",
            "/quit   ",
            "/quit          "
    })
    @DisplayName("quit명령어")
    void quitTest(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Assertions.assertEquals(GameInputReturn.QUIT.getCode(), GameInput.gameInput());
    }
    @ParameterizedTest
    @CsvSource({
            "/save 3   3     ",
            "/save 1       /    ", // 오류 뜨는데 실제로는 이상없음
            "/save 1       /    2",
            "/save 2  4",
            "/save 3 adsfasd",
            "/save 53    "
    })
    @DisplayName("인자 있는 오류 명령어들")
    void cantSaveTest(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Assertions.assertEquals(GameInputReturn.ERROR.getCode(), GameInput.gameInput());
    }

    @ParameterizedTest
    @CsvSource({
            "/save 3        ",
            "/save 1           ",
            "/save 2"
    })
    @DisplayName("save명령어")
    void saveTest(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Assertions.assertEquals(GameInputReturn.SAVE.getCode(), GameInput.gameInput());
        System.out.println(GameInput.number);
    }

    @ParameterizedTest
    @CsvSource({
            "/delsave 3        ",
            "/delsave 1           ",
            "/delsave 2"
    })
    @DisplayName("delsave명령어")
    void delSaveTest(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Assertions.assertEquals(GameInputReturn.DEL_SAVE.getCode(), GameInput.gameInput());
        System.out.println(GameInput.number);
    }

    @ParameterizedTest
    @CsvSource({
            "/load 3        ",
            "/load 1           ",
            "/load 2"
    })
    @DisplayName("load명령어")
    void loadTest(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Assertions.assertEquals(GameInputReturn.LOAD.getCode(), GameInput.gameInput());
        System.out.println(GameInput.number);
    }





}
