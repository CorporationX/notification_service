package faang.school.notificationservice.controller;

import faang.school.notificationservice.dto.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("Entity not found: {}", e.getMessage());
        return new ErrorResponse(e.getMessage() != null ? e.getMessage() : "Entity not found");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Invalid argument: {}", e.getMessage());
        return new ErrorResponse(e.getMessage() != null ? e.getMessage() : "Invalid argument");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    String field = ((FieldError) error).getField();
                    String errorMessage = Objects.requireNonNullElse(error.getDefaultMessage(), "Invalid value");
                    return field + ": " + errorMessage;
                })
                .collect(Collectors.joining("; "));
        log.error("Validation failed: {}", message);
        return new ErrorResponse(message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException e) {
        String errorMessage = e.getConstraintViolations()
                .stream()
                .map(violation -> String.format("Field '%s': %s",
                        violation.getPropertyPath(), violation.getMessage()))
                .collect(Collectors.joining("; "));
        log.error("Constraint violation: {}", errorMessage);
        return new ErrorResponse(errorMessage);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleRuntimeException(Exception e) {
        log.error("Unexpected Runtime error: {}", e.getMessage());
        if (e.getCause() instanceof TelegramApiException telegramApiException) {
            return new ErrorResponse("Failed to send Telegram message: " + telegramApiException.getMessage());
        }
        return new ErrorResponse("An unexpected error occurred: " + e.getMessage());
    }
}
