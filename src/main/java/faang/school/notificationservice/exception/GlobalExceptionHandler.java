package faang.school.notificationservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalExceptions(RuntimeException ex) {
        log.warn("Illegal argument/state detected", ex);
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(NotificationException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Map<String, String> handleNotificationException(NotificationException ex) {
        log.error("Notification service error", ex);
        return Map.of("error", "Notification service temporarily unavailable");
    }

    @ExceptionHandler(MailAuthenticationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleMailAuthenticationException(MailAuthenticationException ex) {
        log.error("Email authentication failed", ex);
        return Map.of("error", "Email service configuration error");
    }

    @ExceptionHandler(MailSendException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Map<String, String> handleMailSendException(MailSendException ex) {
        log.error("Email sending failed", ex);
        return Map.of("error", "Email service temporarily unavailable");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleAllUncaught(Exception ex) {
        log.error("Unhandled exception occurred", ex);
        return Map.of("error", "Internal server error");
    }
}