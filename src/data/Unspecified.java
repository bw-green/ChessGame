package data;

public enum Unspecified {
    CASTLING_FAILED("Castling failed.");

    private final String message;

    Unspecified(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
