package faang.school.notificationservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceCallException.class)
    public void handleServiceCallException(ServiceCallException e) {
        log.error(e.getMessage(), e);
    }
}
