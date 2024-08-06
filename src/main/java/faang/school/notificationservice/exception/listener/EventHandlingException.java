package faang.school.notificationservice.exception.listener;

import static faang.school.notificationservice.exception.ExceptionMessages.EVENT_HANDLING_FAILURE;

public class EventHandlingException extends RuntimeException {
    public EventHandlingException(Throwable cause) {
        super(EVENT_HANDLING_FAILURE, cause);
    }
}