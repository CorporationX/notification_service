package faang.school.notificationservice.exception.handler;

import faang.school.notificationservice.exception.DataValidationException;
import jakarta.persistence.EntityNotFoundException;
import faang.school.notificationservice.exception.IntegrationException;
import faang.school.notificationservice.exception.TelegramChatIdNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerDataValidationException(DataValidationException e){
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerEntityNotFoundException(EntityNotFoundException e){
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(TelegramChatIdNotFound.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTelegramChatIdNotFound(TelegramChatIdNotFound e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(IntegrationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleIntegrationException(IntegrationException e) {
        return new ErrorResponse(e.getMessage());
    }
}
