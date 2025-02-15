package faang.school.notificationservice.exception.impl.non_retryable;

import faang.school.notificationservice.exception.NonRetryableException;

public class ListSizeNotOneException extends NonRetryableException {
    public ListSizeNotOneException(String message) {
        super(message);
    }
}
