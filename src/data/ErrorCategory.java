package data;

import java.util.Optional;

public enum ErrorCategory {
    COMMAND_ERROR,
    FILE_ERROR,
    INVALID_COORDINATES("Try again."),
    UNSPECIFIED;

    private final String suffix;

    ErrorCategory() {
        this.suffix = null;
    }
    ErrorCategory(String suffix) {
        this.suffix = suffix;
    }

    public Optional<String> getSuffix() {
        return Optional.ofNullable(suffix);
    }


}
