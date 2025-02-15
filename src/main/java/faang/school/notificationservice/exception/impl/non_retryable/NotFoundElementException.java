package faang.school.notificationservice.exception.impl.non_retryable;

import faang.school.notificationservice.exception.NonRetryableException;

public class NotFoundElementException extends NonRetryableException {
    public NotFoundElementException(String message) {
        super(message);
    }
}
