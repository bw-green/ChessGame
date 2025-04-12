package data;

public enum Error {// 모든 에러 메세지

    //COMMAND_ERROR
    WRONG_NUMBER(ErrorCategory.COMMAND_ERROR, "CE01", "Wrong number!"),
    WRONG_COMMAND(ErrorCategory.COMMAND_ERROR, "CE02", "Wrong command!"),
    DELSAVE_BLOCK(ErrorCategory.COMMAND_ERROR, "CE03", "The command is blocked."),
      //Error.EMPTY_SLOT.formatMessage([1~5 중 해당하는 수]);
    EMPTY_SLOT(ErrorCategory.COMMAND_ERROR, "CE04", "the save %d is empty."),
    START_BLOCK(ErrorCategory.COMMAND_ERROR, "CE05", "/start is not available during a game."),

    //FILE_ERROR
    FAILED_SAVE(ErrorCategory.FILE_ERROR, "FE01", "Failed to save the game."),
    FAILED_LOAD(ErrorCategory.FILE_ERROR, "FE02", "Failed to load the savefile."),
    FAILED_DELETE(ErrorCategory.FILE_ERROR, "FE03", "Failed to delete the savefile."),

    //INVALID_COORDINATES
    EMPTY_INPUT(ErrorCategory.INVALID_COORDINATES, "IC01", "Empty or null."),
    INCORRECT_LENGTH(ErrorCategory.INVALID_COORDINATES, "IC02", "Coordinates must be 2 characters."),
    INCORRECT_COUNT(ErrorCategory.INVALID_COORDINATES, "IC03", "Exactly 2 coordinates required."),
    INVALID_RANGE(ErrorCategory.INVALID_COORDINATES, "IC04", "Invalid characters in coordinates."),
    SPACE_IN_COORDINATES(ErrorCategory.INVALID_COORDINATES, "IC05", "Space inside coordinates."),
    MUST_BE_DIFFERENT(ErrorCategory.INVALID_COORDINATES, "IC06", "Same START and END square."),
    EMPTY_CELL_START(ErrorCategory.INVALID_COORDINATES, "IC07", "No piece at start square."),
    INCORRECT_SIDE(ErrorCategory.INVALID_COORDINATES, "IC08", "Not your piece."),
    OWN_PIECE_END(ErrorCategory.INVALID_COORDINATES, "IC09", "Own piece at destination."),
    PATH_BLOCKED(ErrorCategory.INVALID_COORDINATES, "IC10", "Path is blocked."),
    INVALID_MOVE(ErrorCategory.INVALID_COORDINATES, "IC11", "Invalid move for this piece."),
    PROMO_NOT_KING(ErrorCategory.INVALID_COORDINATES, "IC12", "King(K, k) cannot be selected for promotion."),
    PROMO_NOT_PAWN(ErrorCategory.INVALID_COORDINATES, "IC13", "Pawn(P, p) cannot be selected for promotion."),
    PROMO_INVALID_PIECE(ErrorCategory.INVALID_COORDINATES, "IC14", "Only Q, R, B or N allowed."),

    //UNSPECIFIED
    CASTLING_FAILED(ErrorCategory.UNSPECIFIED, "UN01", "Castling failed.");

    private final ErrorCategory category;
    private final String code;
    private final String message;

    Error(ErrorCategory category, String code, String message){
        this.category = category;
        this.code = code;
        this.message = message;
    }

    public ErrorCategory getCategory(){
        return category;
    }

    public String getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }

    public String formatMessage(Object... args){
        String baseMessage = String.format(message, args);
        return category.getSuffix().map(suffix -> baseMessage + " " + suffix).orElse(baseMessage);
    }
}
