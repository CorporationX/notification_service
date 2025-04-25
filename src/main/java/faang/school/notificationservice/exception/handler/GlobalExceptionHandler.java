package faang.school.notificationservice.exception.handler;

import faang.school.notificationservice.exception.SmsIntegrationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SmsIntegrationException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse handleSmsIntegrationException(SmsIntegrationException e) {
        return new ErrorResponse(e.getMessage());
    }
}
