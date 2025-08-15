package faang.school.notificationservice.exception;

import org.slf4j.helpers.MessageFormatter;

public class EmptyApiResponseException extends RuntimeException {
    public EmptyApiResponseException(String messagePattern, Object... argArray) {
        super(MessageFormatter.arrayFormat(messagePattern, argArray).getMessage());
    }
}
