package data;

public enum Command {

    /*
    * Command 사용 시
    * Command.HELP, Command.NO_DATA 이런 식으로 쓰면 됩니다.
    *
    * - formatStr 반드시 사용해야 하는 문구
    * SAVEFILE, SAVE, DELSAVE, LOAD
    * REGISTER_SUCCESS, LOGIN_SUCCESS, LOGOUT
    * TOGGLE_ON, TOGGLE_OFF, OPTION
    *
    * start, delsave, login, logout, register, toggle -> 게임 중에는 실행되지 않는 명령어, CommandError.CMD_BLOCK 사용
     */


    // help
    HELP1("/help <1~4>  | /help 1 : show the guide for commands\n" +
            "                       /help <2~4> : show the guide for Chess Variant.\n" +
            "                        each number points to [Three Check/Chaturanga/Pawn Game].\n" +
            "/register | make a new account for this program. \n" +
            "/login | login with an existing account.\n" +
            "/logout | logout from current account.\n" +
            "          The “Guest” account will be applied after logout.\n" +
            "/exit  | exit the program. Unsaved progress can disappear.\n" +
            "/start <1~4> | start a new chess game.\n" +
            "               /start 1 : start a basic chess game.\n" +
            "               /start <2~4> : start a new <Three Check/Chaturanga/Pawn Game>\n" +
            "/quit  | quit the current game and return to the main menu.\n" +
            "         Unsaved progress can disappear.\n" +
            "/save <1~3> | save your game progress to savefile <1~53> if it is empty.\n" +
            "              Your savefile will be overwritten if it is not empty.\n" +
            "/load <1~3> | load and continue your game progress from savefile no.<1~53>.\n" +
            "/delsave <1~3> | !Caution! \n" +
            "                 This command cannot be used during the game.\n" +
            "                 delete your game progress in savefile<1~53>.\n" +
            "/savefile | show your current savefile lists.\n" +
            "/toggle <castling/enpassant/promotion> <on/off> |\n" +
            "             Enable or disable the received special rule.\n" +
            "/option | show the special rule state applying to next basic chess game.\n" +
            "-------------------------------------------------- \n" +
            "A collection of commands for quick execution.\n"),
    HELP2("In 3 Check Chess, \n" +
            "you win if you check your opponent's king three times or deliver checkmate,\n" +
            "stalemate is possible, and insufficient material only applies \n" +
            "when there are two kings left.\n"),
    HELP3("A Pawn(F,f), if white, can move +1 rank, and if black, can move -1 rank.\n" +
            "If a pawn is located on rank 1 or rank 8, it must be promoted, and it becomes a Mantri.\n" +
            "\n" +
            "A Mantri(M,m) is only allowed to move one square diagonally — \n" +
            "upper-right, lower-right, upper-left, or lower-left.\n" +
            "\n" +
            "A Gaja(G,g)is only allowed to move two squares diagonally — \n" +
            "upper-right, lower-right, upper-left, or lower-left.\n" +
            "It can move even if there is a piece in between.\n"),
    HELP4("The Pawn Game is a variant of standard chess in which all pieces\n" +
            "except pawns and kings are removed, so each player has only eight pawns and one king. \n" +
            "\n" +
            "In the current implementation of the Pawn Game,\n" +
            "special movement rules such as “Knockback” and\n" +
            "a random pawn placement at the start of the game have also been added.\n"),


    //savefile : 지울 예정
    SAVEFILE("-----------------<last save file>---------------—\n" +
            "|save %d.|%s\n" +
            " -------------------------------------------------\n" +
            "|save 1.|<%s> \n" +
            "|save 2.|<%s> \n" +
            "|save 3.|<%s> \n" +
            "-------------------------------------------------\n" +
            "|the last save file and the list of save files"),
    NO_DATA("No Data"),

    // 계정 관련 명령어 출력 문구
    INPUT_ID(" ID :"),
    INPUT_PW(" PassWord :"),
    ACC_INPUT_TERMINATE("|Input process terminated.|"),
    REGISTER_START("|Please enter an ID and PassWord to register. \n" +
            " The ID must be unique.|"),
    REGISTER_SUCCESS("|User %s has been registered|"),
    LOGIN_START("|Please enter an ID and PassWord to login.|"),
    LOGIN_SUCCESS("|User %s has been logged in|"),
    LOGOUT("|User %s has been logged out.|"),

    // 특수 규칙 관련 명령어 출력 문구
    TOGGLE_ON("%s activated"),
    TOGGLE_OFF("%s deactivated"),
    OPTION("setting                     toggle\n" +
            "\n" +
            "promotion                    %s\n" +
            "enpassant                    %s\n" +
            "castling                     %s\n"),

    //예/아니오 대답

    YES_OR_NO_EXIT(" Would you exit? (y/n) : "),
    YES_OR_NO_QUIT(" Would you quit? (y/n) : "),
    YES_OR_NO_LOAD(" Would you load? (y/n) : "),

    // 저장 안되었을 때
    YES_OR_NO_WHILE_NOT_SAVED("The last saved file is not current board. Would you continue? (y/n)"),
    //exit
    EXIT("| Exit command received. Closing the game. |"), //내용 없지만 명시 위해 놔둠

    //quit
    QUIT("Successfully quited game."),

    //save
    SAVE("|The save %d has been created.|"),

    //load
    LOAD("| The save %d has loaded |\n" +
            "|save %d.|< %s >"),

    //delsave
    DELSAVE("| The save %d has deleted |"),

    //start 없어도 되는데 지울지 말지 고민중
    START(""); //내용 없지만 명시 위해 놔둠



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
            //int 인자 1개
            case SAVE, DELSAVE -> {
                if (args.length == 1 && args[0] instanceof Integer arg && arg >= 1 && arg <= 5) {
                    yield String.format(this.toString(), arg);
                }
                throw new IllegalArgumentException(this + " requires a number between 1 and 5.");
            }

            //String 인자 1개
            case REGISTER_SUCCESS, LOGIN_SUCCESS, LOGOUT-> {
                if (args.length == 1 && args[0] instanceof String) {
                    yield String.format(this.toString(), args[0]);
                }
                throw new IllegalArgumentException(this + " ");
            }

            //int 인자 2개, string 1개
            case LOAD -> {
                if (args.length == 3 &&
                        args[0] instanceof Integer &&
                        args[1] instanceof Integer &&
                        args[2] instanceof String) {
                    yield String.format(this.toString(), args);
                }
                throw new IllegalArgumentException("LOAD requires (int, int, String).");
            }

            //toggle 인자 int 1개, boolean 1개
            case TOGGLE_ON, TOGGLE_OFF ->{
                if (args.length == 1 && args[0] instanceof  Integer arg && arg >= 0 && arg <= 2) {
                    String str = switch (arg) {
                        case 0 -> "Enpassant";
                        case 1 -> "Castling";
                        case 2 -> "Promotion";
                        default -> "";
                    };
                    yield String.format(this.toString(), str);
                }
                throw new IllegalArgumentException("OPTION requires 3 string arguments.");
            }

            //option의 인자는 무조건 boolean 인자 3개
            case OPTION -> {
                if (args.length == 3 &&
                        args[0] instanceof Boolean &&
                        args[1] instanceof Boolean &&
                        args[2] instanceof Boolean) {
                    String[] strs = {"", "", ""};
                    strs[0] = (Boolean)args[0] ? "ON" : "OFF";
                    strs[1] = (Boolean)args[1] ? "ON" : "OFF";
                    strs[2] = (Boolean)args[2] ? "ON" : "OFF";

                    yield String.format(this.toString(), strs);
                }
                throw new IllegalArgumentException("OPTION requires 3 string arguments.");
            }
            default -> throw new UnsupportedOperationException("This command does not support formatted messages.");
        };
    }
}
