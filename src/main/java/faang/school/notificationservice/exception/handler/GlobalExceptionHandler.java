package faang.school.notificationservice.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailParseException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

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
