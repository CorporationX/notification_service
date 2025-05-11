package faang.school.notificationservice.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import faang.school.notificationservice.exception.NotificationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.warn("⚠️ IllegalArgumentException: {}", e.getMessage(), e);
        return ErrorResponse.builder()
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .url(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(NotificationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleNotificationException(NotificationException e, HttpServletRequest request) {
        log.error("📬 NotificationException: {}", e.getMessage(), e);
        return ErrorResponse.builder()
                .message(e.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .url(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnhandled(Exception e, HttpServletRequest request) {
        log.error("🔥 Unexpected error: {}", e.getMessage(), e);
        return ErrorResponse.builder()
                .message("Internal server error")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .url(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleNotificationException(RuntimeException e, HttpServletRequest request) {
        log.error("RuntimeException: {}", e.getMessage(), e);
        return ErrorResponse.builder()
                .message(e.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .url(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(JsonProcessingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotificationException(JsonProcessingException e, HttpServletRequest request) {
        log.error("JsonProcessingException: {}", e.getMessage(), e);
        return ErrorResponse.builder()
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .url(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleNotificationException(NullPointerException e, HttpServletRequest request) {
        log.error("NullPointerException: {}", e.getMessage(), e);
        return ErrorResponse.builder()
                .message(e.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .url(request.getRequestURI())
                .build();
    }
}
