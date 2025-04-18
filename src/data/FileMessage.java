package data;

public enum FileMessage {
    SAVE_CREATED("| The save %d has been created.|"),
    SAVE_DELETED("| The save %d has deleted |"),
    SAVE_LOADED("| The save %d has loaded |"),
    LSF_LINE("-----------------<Last Save File>-----------------"),
    SAVE_NAME("|save %d.|< %s >"),
    LSF_NAME("|save %s.| %s");

    private final String message;

    FileMessage(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

    public String format(Object... args) {
        return String.format(this.message, args);
    }
}

