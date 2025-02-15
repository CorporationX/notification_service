package faang.school.notificationservice.exception.impl.retryable;

import faang.school.notificationservice.exception.RetryableException;

public class UserServiceClientException extends RetryableException {
    public UserServiceClientException(String message) {
        super(message);
    }
}
