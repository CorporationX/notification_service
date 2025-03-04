package faang.school.notificationservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MailException.class)
    public void handleMailException(MailException e) {
        log.error("Error sending email: {}", e.getMessage());
    }
}
