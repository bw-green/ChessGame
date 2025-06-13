package data;

public enum CommandError {

    WRONG_NUMBER("Wrong number!"),
    WRONG_COMMAND("Wrong command!"),

    //start, delsave, login, logout, register, toggle
    CMD_BLOCK("The command is blocked."),

    // 계정 관련 명령어 : GameManager 에서만 사용 예정
    ACC_INVALID_INPUT("Invalid input:Input must be between 2 and 10 characters."),
    ACC_BANNED_STR("Invalid input:This input is not allowed"),
    ID_EXISTING("|User %s is already been registered|"),
    ID_INVALID("|User %s cannot be found|"),
    PW_UNMATCH("|Password does not match|"),
    LOGIN_FAIL("|Login failed: a user is already connected to the system.|"),
    LOGOUT_FAIL("|Logout failed: no active session.|"),

    //Error.EMPTY_SLOT.formatMessage([1~5 중 해당하는 수]);
    EMPTY_SLOT("the save %d is empty."),
    START_BLOCK("| /start is not available during a game! |");

    private final String message;

    CommandError(String message){
        this.message = message;
    }

    // int 인자가 1개 있는 에러메세지에 적용
    public String formatMessage(int arg){
        if(this == EMPTY_SLOT) {
            if (arg >= 0 && arg <= 5)
                return String.format(message, arg);
            else
                throw new NumberFormatException("slot index out of bounds");
        }
        return message;
    }

    public String formatMessage(String arg){
        if(this == ID_EXISTING || this == ID_INVALID){
            return String.format(message, arg);
        }else{
            throw new NumberFormatException("UnMatched Command Error");
        }
    }

    //formatMessage를 int 인자를 받는 메서드로 오버로드하기 위한 원형
    public String formatMessage(Object... args){
        return message;
    }

    @Override
    public String toString() {
        if(this == EMPTY_SLOT)
            throw new NullPointerException("slot argument is empty");
        return message;
    }
}
