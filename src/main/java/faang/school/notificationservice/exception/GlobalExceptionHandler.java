package faang.school.notificationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DataValidationException.class)
    public ResponseEntity<Object> handleDataValidationException(DataValidationException ex) {
        return new ResponseEntity<>(response(ex), HttpStatus.BAD_REQUEST);
    }

    private Map<String, String> response(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("time", LocalDateTime.now().toString());
        return response;
    }
}
