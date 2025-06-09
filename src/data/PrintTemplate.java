package data;


public enum PrintTemplate {

    // 미로그인 상태
    GUEST("Guest"),

    //프롬프트 입력 창
    MENU_PROMPT("menu >"),
    BLACK_PROMPT("BLACK >"),
    WHITE_PROMPT("WHITE >"),
    //구분선
    BOLDLINE("=================================================="),
    INTERLINE("--------------------------------------------------"),
    MENULINE("===================<MAIN  MENU>==================="),

    CURRENT_USER(":: Current user: "),

    //메뉴 지시문
    MENU_FIRST_INSTRUCT("|type “/help 1” to learn how to use this program."),
    MENU_LAST_SAVE("|the last save file and the list of save files"),

    //게임 지시문
    GAME_BASE_INSTRUCT("Enter the starting and ending positions of the piece (e.g., \"e2 e4\")"),

    //세이브 관련 경고문
    GAME_NOT_SAVED("| The last saved file is not current board |"),
    GAME_SAVED("| The current board matches the latest save file. |"),

    //체크 경고문
    CHECK_BLACK("Check : Black"),
    CHECK_WHITE("Check : White"),

    //게임 종료문
    END_WHITE_CHECKMATE("Checkmate! WHITE wins. The game has ended.\n" +
            "Returning to the main menu."),
    END_BLACK_CHECKMATE("Checkmate! BLACK wins. The game has ended.\n" +
            "Returning to the main menu."),
    END_STALEMATE("Stalemate! No legal moves available.\n" +
            "The game ends in a draw. Returning to the main menu."),
    END_INSUFFICIENT("Insufficient material to checkmate.\n" +
            "The game ends in a draw. Returning to the main menu."),
    THREE_CHECK_BLACK("BLACK has achieved three checks! BLACK wins. The game has ended.\n" +
            "Returning to the main menu."),
    THREE_CHECK_WHITE("WHITE has achieved three checks! BLACK wins. The game has ended.\n" +
            "Returning to the main menu."),
    END_WHITE_STALEMATE("StaleMate! No legal moves available. WHITE wins.\n" +
            "The game has ended. Returning to the main menu."),
    END_BLACK_STALEMATE("StaleMate! No legal moves available. BLACK wins.\n" +
            "The game has ended. Returning to the main menu.");


    private final String printTmp;
    PrintTemplate(String printTmp){
        this.printTmp = printTmp;
    }


    @Override
    public String toString() {
        return printTmp;
    }
}
