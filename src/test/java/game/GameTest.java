package test.java.game;

import data.MoveResult;
import gameManager.GameManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;

public class GameTest {


    @ParameterizedTest
    @CsvSource({
            "/start  \n g1 f3",
            "a7 a5 ",
            "e2 e4 " ,
            "b7 b6 " ,
            "f1 a6 " ,
            " a8 a6" ,
            " h1 f1 " ,
            "h7 h5 " ,
            "f1 h1" ,
            "g7 g6" ,
            "e1 g1"

    })
    @DisplayName("킹사이드 : 룩이 한번 움직임")
    void CanCastlingTest1(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        new GameManager();
    }
}

