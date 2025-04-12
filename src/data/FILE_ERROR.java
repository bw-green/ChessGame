package data;

public enum FILE_ERROR {
    FAILED_SAVE("Failed to save the game."),
    FAILED_LOAD("Failed to load the savefile."),
    FAILED_DELETE("Failed to delete the savefile.");

    private final String message;

    FILE_ERROR(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
