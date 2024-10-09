package faang.school.notificationservice.exception;

import faang.school.notificationservice.dto.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDataValidationException(DataValidationException ex) {
        log.error("Error code {}: message: {}", HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(SmsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSmsException(SmsException ex) {
        log.error("Error code {}: message: {}", ex.getMessageStatus(), ex.getMessage());
        return new ErrorResponse(ex.getMessageStatus().getMessageStatus(), ex.getMessage());
    }
}
