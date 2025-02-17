package faang.school.notificationservice.exception.impl.non_retryable;

import faang.school.notificationservice.exception.NonRetryableException;

public class DtoValidationFailedException extends NonRetryableException {
    public DtoValidationFailedException(String message) {
        super(message);
    }
}
