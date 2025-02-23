package faang.school.notificationservice.exception.handler;


import faang.school.notificationservice.exception.IntegrationException;
import faang.school.notificationservice.exception.TelegramChatIdNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TelegramChatIdNotFound.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTelegramChatIdNotFound(TelegramChatIdNotFound e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(IntegrationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIntegrationException(IntegrationException e) {
        return new ErrorResponse(e.getMessage());
    }
}
