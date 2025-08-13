package faang.school.notificationservice.exception;

import faang.school.notificationservice.dto.ErrorType;
import org.slf4j.helpers.MessageFormatter;

public class ProcessorNotFoundException extends NonRetryableException {
    public ProcessorNotFoundException(String messagePattern, Object... argArray) {
        super(MessageFormatter.arrayFormat(messagePattern, argArray).getMessage());
    }

    public ProcessorNotFoundException(ErrorType errorType) {
        super(errorType.getErrorMessage());
    }
}
