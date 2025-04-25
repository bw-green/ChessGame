package data;

public enum GameInputReturn {
    ERROR(0),
    HELP(1),
    EXIT(2),
    START(3),
    QUIT(4),
    SAVE(5),
    LOAD(6),
    DEL_SAVE(7),
    SAVE_FILE(8),

    COORDINATE_TRUE(9);

    private final int code;

    GameInputReturn(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
