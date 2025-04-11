package faang.school.notificationservice.messages;

public class ErrorMessages {
    //AbstractEvenListener
    public static final String ERROR_DESERIALIZING_MESSAGE = "Error deserializing message {}";
    public static final String NO_MESSAGE_BUILDER_FOUND_FOR_LOCALE = "No message builder found for locale %s";
    public static final String NO_NOTIFICATION_SERVICE_FOUND_FOR_PREFERRED_COMMUNICATION = "No notification service found for %s preferred communication method";

    //Kafka consumer
    public static final String FAILED_TO_ACKNOWLEDGE_KAFKA_MESSAGE = "Failed to acknowledge Kafka message";
}
