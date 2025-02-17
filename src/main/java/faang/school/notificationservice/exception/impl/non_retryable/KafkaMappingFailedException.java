package faang.school.notificationservice.exception.impl.non_retryable;

import faang.school.notificationservice.exception.NonRetryableException;

public class KafkaMappingFailedException extends NonRetryableException {
    public KafkaMappingFailedException(String message) {
        super(message);
    }
}
