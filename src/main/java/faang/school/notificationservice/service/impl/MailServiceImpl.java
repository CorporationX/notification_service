package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.config.email.MailProperties;
import faang.school.notificationservice.exception.EmailSendingException;
import faang.school.notificationservice.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private final MailProperties mailProperties;

    @Retryable(
            retryFor = {MailException.class, MessagingException.class},
            maxAttemptsExpression = "${spring.mail.retry.max-attempts:3}",
            backoff = @Backoff(
                    delayExpression = "${spring.mail.retry.backoff:1000}",
                    multiplierExpression = "${spring.mail.retry.multiplier:2}"
            )
    )
    @Override
    @Async("mailExecutor")
    public void sendEmail(String to, String subject, String text) {
        validateEmail(to);
        try {
            MimeMessage message = createMimeMessage(to, subject, text);
            javaMailSender.send(message);
            log.info("Email sent successfully to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to create email for '{}': {}", to, e.getMessage(), e);
            throw new EmailSendingException("Email creation failed", e);
        } catch (MailException e) {
            log.error("Sending email to '{}' failed: {}", to, e.getMessage(), e);
            throw new EmailSendingException("Email send failed", e);
        }
    }

    @Recover
    public void recoverSendEmail(Exception e, String to, String subject, String text) {
        log.warn("Recovering failed email send to '{}'. Exception: {}", to, e.getMessage(), e);
    }

    private void validateEmail(String email) {
        if (!EmailValidator.getInstance().isValid(email)) {
            log.warn("Invalid email address provided: {}", email);
            throw new IllegalArgumentException("Invalid email address: " + email);
        }
    }

    private MimeMessage createMimeMessage(String to, String subject, String text)
            throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

        helper.setFrom(mailProperties.getFrom());
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);
        helper.setReplyTo(mailProperties.getReplyTo());

        return message;
    }
}
