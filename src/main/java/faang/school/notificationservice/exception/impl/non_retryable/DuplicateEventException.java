package faang.school.notificationservice.exception.impl.non_retryable;

import faang.school.notificationservice.exception.NonRetryableException;

public class DuplicateEventException extends NonRetryableException {
    public DuplicateEventException(String message) {
        super(message);
    }
}
