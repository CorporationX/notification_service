package faang.school.notificationservice.exception;

public enum MessageError {
    UNABLE_TO_PARSE_EVENT("Unable to parse event: %s with message: %s.");

    private final String message;

    MessageError(String message) {
        this.message = message;
    }

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
