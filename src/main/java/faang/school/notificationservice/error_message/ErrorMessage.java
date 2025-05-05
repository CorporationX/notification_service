package faang.school.notificationservice.error_message;

public class ErrorMessage {
    public static final String ERROR_NOTIFICATION = "Error when processing the notification: {}\n";
    public static final String ERROR_NOT_FOUND_NOTIFICATION = "No notification service found for the user's" +
            "preferred communication method.\n";

    private static final String ERROR_NOT_FOUND_MESSAGE_BUILDER = "No message builder found for the given event type: %s\n";

    public static String getErrorNotFoundMessageBuilder(String nameBuilder) {
        return String.format(ERROR_NOT_FOUND_MESSAGE_BUILDER, nameBuilder);
    }
}
