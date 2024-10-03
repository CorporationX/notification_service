package faang.school.notificationservice.exception;

public final class ExceptionMessages {

    private ExceptionMessages() {
    }

    public static final String EVENT_HANDLING_FAILURE = "An error occurred while handling the event.";
    public static final String NO_MESSAGE_BUILDER_FOUND = "No appropriate message builder found for event: %s";
    public static final String NO_NOTIFICATION_SERVICE_FOUND = "No appropriate notification service found for user: %s";
    public static final String USER_DATA_FETCH_FAILURE = "Failed to fetch user data for user id: %s";
    public static final String SMS_SENDING_FAILURE = "Failed to send sms for user id %s. Network error.";
    public static final String TELEGRAM_SENDING_ERROR = "Error sending message: {}";
    public static final String INVALID_PHONE_NUMBER = "Failed to send sms for user id %s. The number is invalid.";
}
