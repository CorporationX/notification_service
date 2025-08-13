package faang.school.notificationservice.exception;

import faang.school.notificationservice.dto.ErrorType;
import org.slf4j.helpers.MessageFormatter;

public class NotificationServiceNotFoundException extends NonRetryableException {
    public NotificationServiceNotFoundException(String messagePattern, Object... argArray) {
        super(MessageFormatter.arrayFormat(messagePattern, argArray).getMessage());
    }

    public NotificationServiceNotFoundException(ErrorType errorType) {
        super(errorType.getErrorMessage());
    }
}
