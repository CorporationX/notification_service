package faang.school.notificationservice.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.mail.MailParseException;

import java.io.IOException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IOException.class)
    public ResponseEntity<Object> handleJsonProcessingExceptions(IOException ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .internalServerError()
                .body(ex.getMessage());
    }

    @ExceptionHandler(MailParseException.class)
    public ResponseEntity<Object> handleWrongEmails(MailParseException ex) {
        log.error("Wrong email format", ex);
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleWrongEmails(NullPointerException ex) {
        log.error("This field cannot be null", ex);
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
