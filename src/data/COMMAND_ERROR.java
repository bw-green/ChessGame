package data;

public enum COMMAND_ERROR {

    WRONG_NUMBER("Wrong number!"),
    WRONG_COMMAND("Wrong command!"),
    DELSAVE_BLOCK("The command is blocked."),

    //Error.EMPTY_SLOT.formatMessage([1~5 중 해당하는 수]);
    EMPTY_SLOT("the save %d is empty."),
    START_BLOCK("/start is not available during a game.");

    private final String message;

    COMMAND_ERROR(String message){
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
