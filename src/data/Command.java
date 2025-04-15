package data;

public enum Command {

    /*
    * Command 사용 시
    * Command.HELP, Command.NO_DATA 이런 식으로 쓰면 됩니다.
    * SAVEFILE, SAVE, DELSAVE, LOAD는 반드시 formatStr로 인자 넘겨주셔야 됩니다.
    *
    */


    //공동 사용
    HELP("/help | show the guide for commands\n" +
            "/exit | exit the program. Unsaved progress can disappear.\n" +
            "/start | start a new basic rule chess game.\n" +
            "/quit | quit the current game and return to the main menu.\n" +
            "        Unsaved progress can disappear.\n" +
            "/save <1~5> | save your game progress to savefile <1~5> if it is empty.\n" +
            "              Your savefile will be overwritten if it is not empty.\n" +
            "/load <1~5> | load and continue your game progress from savefile no.<1~5>.\n" +
            "/delsave <1~5> | !Caution!\n" +
            "                 This command cannot be used during the game.\n" +
            "                 delete your game progress in savefile<1~5>.\n" +
            "/savefile | show your current savefile lists.\n"+
            "-------------------------------------------------\n" +
            "A collection of commands for quick execution."),
    SAVEFILE("-----------------<last save file>---------------—\n" +
            "|save %d.|%s\n" +
            " -------------------------------------------------\n" +
            "|save 1.|<%s> \n" +
            "|save 2.|<%s> \n" +
            "|save 3.|<%s> \n" +
            "|save 4.|<%s> \n" +
            "|save 5.|<%s> \n"+
            "-------------------------------------------------\n" +
            "|the last save file and the list of save files"),
    NO_DATA("No Data"),
    YES_OR_NO_EXIT("Would you exit? (y/n)"),
    YES_OR_NO_QUIT("Would you quit? (y/n)"),
    YES_OR_NO_LOAD("Would you load? (y/n)"),

    EXIT("Successfully exited program."), //내용 없지만 명시 위해 놔둠
    QUIT("Successfully quited program."), //내용 없지만 명시 위해 놔둠
    SAVE("|The save %d has been created.|"),
    LOAD("| The save %d has loaded |\n" +
            "|save %d.|< %s >"),

    //메뉴에서만 사용
    DELSAVE("| The save %d has deleted |"),
    START(""),  //내용 없지만 명시 위해 놔둠

    // 저장 안되었을 때
    YES_OR_NO_WHILE_NOT_SAVED("The last saved file is not current board. Would you continue? (y/n)");

    private final String cmdMessage;

    Command(String cmdMessage) {
        this.cmdMessage = cmdMessage;
    }

    @Override
    public String toString() {
        return cmdMessage;
    }


    public String formatStr(Object... args){
        return switch (this) {
            case SAVE, DELSAVE -> {
                if (args.length == 1 && args[0] instanceof Integer arg && arg >= 1 && arg <= 5) {
                    yield String.format(this.toString(), arg);
                }
                throw new IllegalArgumentException(this + " requires a number between 1 and 5.");
            }
            case LOAD -> {
                if (args.length == 3 &&
                        args[0] instanceof Integer &&
                        args[1] instanceof Integer &&
                        args[2] instanceof String) {
                    yield String.format(this.toString(), args);
                }
                throw new IllegalArgumentException("LOAD requires (int, int, String).");
            }
            case SAVEFILE -> {
                if (args.length == 5 &&
                        args[0] instanceof String &&
                        args[1] instanceof String &&
                        args[2] instanceof String &&
                        args[3] instanceof String &&
                        args[4] instanceof String) {
                    yield String.format(this.toString(), args);
                }
                throw new IllegalArgumentException("SAVEFILE requires 5 string arguments.");
            }
            default -> throw new UnsupportedOperationException("This command does not support formatted messages.");
        };
    }
}
