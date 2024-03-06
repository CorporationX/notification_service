package faang.school.notificationservice.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(Exception exception, HttpServletRequest request) {
        log.error("Error: {}", exception);
        return getErrorResponse(request.getRequestURI(), HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler({IOException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleIOException(Exception exception, HttpServletRequest request) {
        log.error("Error: {}", exception);
        return getErrorResponse(request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }


    private ErrorResponse getErrorResponse(String url, HttpStatus status, String message) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .url(url)
                .status(status)
                .message(message)
                .build();
    }
}
