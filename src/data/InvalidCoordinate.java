package data;

public enum InvalidCoordinate {
    EMPTY_INPUT("Empty or null."),
    INCORRECT_LENGTH("Coordinates must be 2 characters."),
    INCORRECT_COUNT("Exactly 2 coordinates required."),
    INVALID_RANGE("Invalid characters in coordinates."),
    SPACE_IN_COORDINATES("Space inside coordinates."),
    MUST_BE_DIFFERENT("Same START and END square."),
    EMPTY_CELL_START("No piece at start square."),
    INCORRECT_SIDE("Not your piece."),
    OWN_PIECE_END("Own piece at destination."),
    PATH_BLOCKED("Path is blocked."),
    INVALID_MOVE("Invalid move for this piece."),
    PROMO_NOT_KING("King(K, k) cannot be selected for promotion."),
    PROMO_NOT_PAWN("Pawn(P, p) cannot be selected for promotion."),
    PROMO_INVALID_PIECE("Only Q, R, B or N allowed."),
    HAS_SPACE("Space inside coordinate.");

    private final String message;

    InvalidCoordinate(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        String SUFFIX = " Try again.";
        return message + SUFFIX;
    }
}
