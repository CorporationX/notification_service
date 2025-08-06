package faang.school.notificationservice.exception;

import org.slf4j.helpers.MessageFormatter;

public class MessageSendException extends RuntimeException {
    public MessageSendException(String messagePattern, Object... argArray) {
        super(MessageFormatter.arrayFormat(messagePattern, argArray).getMessage());
    }
}
