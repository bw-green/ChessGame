package data;

public enum FileError {
    FAILED_SAVE("Failed to save the game."),
    FAILED_LOAD("Failed to load the savefile."),
    FAILED_DELETE("Failed to delete the savefile.");

    private final String message;

    FileError(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
