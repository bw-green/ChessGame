package test.java.gameManager;

import gameManager.GameManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class GMTest {
    static GameManager gm;


    public static void main(String[] args){
        gm = new GameManager();
    }

    static void setUp(){  }

    public void test(String cmd){
        gm = new GameManager();
    }
}
