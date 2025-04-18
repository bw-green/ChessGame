package data;

public enum FileError {
    FAILED_SAVE("Failed to save the game."),
    FAILED_LOAD("Failed to load the savefile."),
    FAILED_DELETE("Failed to delete the savefile."),
    FAILED_MAKDIR("Failed to make a directory."),
    FAILED_MAKDIR_ERROR("Unable to create save directory. The program will terminate."),

    ///아래부터는 에러 캐치시 디버깅용 출력문들입니다.
    DEBUG_ERROR_OVERWRITE("Error writing to the save file."),
    DEBUG_ERROR_DELETE("Error deleting the save file."),
    DEBUG_ERROR_LOAD("Error loading the save file."),
    DEBUG_ERROR_LOAD_FN("Error getting the file names");

    private final String message;

    FileError(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
