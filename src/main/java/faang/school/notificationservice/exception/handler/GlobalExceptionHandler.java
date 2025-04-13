package faang.school.notificationservice.exception.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import faang.school.notificationservice.exception.MappingException;
import faang.school.notificationservice.exception.MessageBuilderNotFoundException;
import faang.school.notificationservice.exception.ServiceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MappingException.class)
    ResponseEntity<ErrorResponse> handleMappingException(MappingException e) {
        String message = e.getMessage();
        Throwable cause = e.getCause() != null ? e.getCause() : e;
        HttpStatus status;
        if (cause instanceof JsonParseException) {
            log.error("Bad request, invalid data: {}", message, e);
            status = HttpStatus.BAD_REQUEST;
        } else if (cause instanceof JsonMappingException) {
            log.error("Unprocessable entity, mapping error: {}", message, e);
            status = HttpStatus.UNPROCESSABLE_ENTITY;
        } else {
            log.error("Internal server error, unexpected error: {}", message, e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        ErrorResponse response = buildResponse(e);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(MessageBuilderNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ErrorResponse handleMessageBuilderNotFoundException(MessageBuilderNotFoundException e) {
        log.error("MessageBuilderNotFoundException: {}", e.getMessage());
        return buildResponse(e);
    }

    @ExceptionHandler(ServiceNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ErrorResponse handleServiceNotFoundException(ServiceNotFoundException e) {
        log.error("ServiceNotFoundException: {}", e.getMessage());
        return buildResponse(e);
    }

    private ErrorResponse buildResponse(Exception e) {
        log.error(e.getClass().getSimpleName(), e);
        return ErrorResponse.builder()
                .timeStamp(LocalDateTime.now())
                .error(e.getClass().getName())
                .message(e.getMessage())
                .build();
    }
}
