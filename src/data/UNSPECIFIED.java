package data;

public enum UNSPECIFIED {
    CASTLING_FAILED("Castling failed.");

    private final String message;

    UNSPECIFIED(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
