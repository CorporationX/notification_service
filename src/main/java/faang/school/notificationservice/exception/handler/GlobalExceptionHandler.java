package faang.school.notificationservice.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import faang.school.notificationservice.exception.EmailSendingException;
import faang.school.notificationservice.exception.DataValidationException;
import faang.school.notificationservice.exception.NotificationException;
import faang.school.notificationservice.exception.SmsIntegrationException;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(EmailSendingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleEmailSendingException(EmailSendingException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(MessagingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleMessagingException(MessagingException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleIOException(IOException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleRuntimeException(RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(SmsIntegrationException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse handleSmsIntegrationException(SmsIntegrationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(DataValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerDataValidationException(DataValidationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerEntityNotFoundException(EntityNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(NotificationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleNotificationException(NotificationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(JsonProcessingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleJsonProcessingException(JsonProcessingException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleNullPointerException(NullPointerException e) {
        return new ErrorResponse(e.getMessage());
    }
}
