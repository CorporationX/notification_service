package faang.school.notificationservice.exception;

public final class ExceptionMessages {

    private ExceptionMessages() {
    }

    public static final String EVENT_HANDLING_FAILURE = "An error occurred while handling the event.";
    public static final String NO_MESSAGE_BUILDER_FOUND = "No appropriate message builder found for event: %s";
    public static final String NO_NOTIFICATION_SERVICE_FOUND = "No appropriate notification service found for user: %s";
    public static final String USER_DATA_FETCH_FAILURE = "Failed to fetch user data for user id: %s";
}
