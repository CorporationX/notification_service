package faang.school.notificationservice.service;

import faang.school.notificationservice.config.email.EmailProperties;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.EmailMessageCreationException;
import faang.school.notificationservice.exception.EmailSendingException;
import faang.school.notificationservice.exception.InvalidEmailFormatException;
import faang.school.notificationservice.exception.InvalidUserException;
import faang.school.notificationservice.exception.MessageBuilderNotFoundException;
import faang.school.notificationservice.messaging.MessageBuilder;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService {

    private final JavaMailSender mailSender;
    private final List<MessageBuilder<?>> messageBuilders;
    private final EmailProperties emailProperties;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void send(UserDto user, String message) {
        validateUser(user);

        try {
            MimeMessage mimeMessage = createMimeMessage(user, message);
            log.info("Sending email to user: {} ({})", user.getUsername(), user.getEmail());
            mailSender.send(mimeMessage);
            log.info("Email successfully sent to: {}", user.getEmail());

        } catch (MessagingException e) {
            log.error("Failed to create email message for user: {}", user.getEmail(), e);
            throw new EmailMessageCreationException("Failed to create email message", e);
        } catch (MailException e) {
            log.error("Failed to send email to: {}", user.getEmail(), e);
            throw new EmailSendingException("Failed to send email notification", e);
        }
    }

    public <T> void sendEvent(UserDto user, T event, Locale locale) {
        validateUser(user);
        MessageBuilder<T> builder = findMessageBuilder(event.getClass());
        String message = builder.buildMessage(event, locale != null ? locale : Locale.getDefault());
        send(user, message);
    }

    public <T> void sendEvent(UserDto user, T event) {
        sendEvent(user, event, Locale.getDefault());
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }

    private MimeMessage createMimeMessage(UserDto user, String message) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                true,
                StandardCharsets.UTF_8.name()
        );

        String fromAddress;
        try {
            String fromName = emailProperties.getFromName();
            if (fromName != null && !fromName.isEmpty()) {
                fromAddress = String.format("%s <%s>", fromName, emailProperties.getFrom());
            } else {
                fromAddress = String.format("Notification Service <%s>", emailProperties.getFrom());
            }
        } catch (Exception e) {
            fromAddress = String.format("Notification Service <%s>", emailProperties.getFrom());
        }

        helper.setFrom(fromAddress);
        helper.setTo(user.getEmail());
        helper.setSubject(generateSubject());

        String htmlContent = buildHtmlContent(user, message);
        helper.setText(htmlContent, true);

        mimeMessage.setHeader("X-Priority", "3");
        mimeMessage.setHeader("X-Mailer", "Spring Boot Notification Service");

        return mimeMessage;
    }

    @SuppressWarnings("unchecked")
    private <T> MessageBuilder<T> findMessageBuilder(Class<?> eventClass) {
        return (MessageBuilder<T>) messageBuilders.stream()
                .filter(builder -> builder.getInstance().isAssignableFrom(eventClass))
                .findFirst()
                .orElseThrow(() -> new MessageBuilderNotFoundException(
                        "No message builder found for event type: " + eventClass.getName()
                ));
    }

    private void validateUser(UserDto user) {
        if (user == null) {
            throw new InvalidUserException("User cannot be null");
        }

        if (!StringUtils.hasText(user.getEmail())) {
            throw new InvalidUserException(
                    String.format("Email is not set for user: %s (id: %d)",
                            user.getUsername(), user.getId())
            );
        }

        if (!isValidEmail(user.getEmail())) {
            throw new InvalidEmailFormatException(
                    String.format("Invalid email format for user %s: %s",
                            user.getUsername(), user.getEmail())
            );
        }
    }

    private boolean isValidEmail(String email) {
        return email != null &&
                email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    private String generateSubject() {
        return String.format("Notification - %s",
                LocalDateTime.now().format(DATE_FORMATTER));
    }

    private String buildHtmlContent(UserDto user, String message) {
        return String.format("""
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <meta charset="UTF-8">
                            <style>
                                body { font-family: Arial, sans-serif; color: #333; }
                                .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                                .header { background: #4CAF50; color: white; padding: 20px; text-align: center; }
                                .content { background: #f9f9f9; padding: 20px; border: 1px solid #ddd; }
                                .message { background: white; padding: 15px; margin: 15px 0; border-left: 4px solid #4CAF50; }
                                .footer { text-align: center; color: #666; font-size: 12px; margin-top: 20px; }
                            </style>
                        </head>
                        <body>
                            <div class="container">
                                <div class="header">
                                    <h2>Notification Service</h2>
                                </div>
                                <div class="content">
                                    <p>Hello, <strong>%s</strong>!</p>
                                    <div class="message">%s</div>
                                </div>
                                <div class="footer">
                                    <p>Sent at: %s</p>
                                </div>
                            </div>
                        </body>
                        </html>
                        """,
                user.getUsername() != null ? user.getUsername() : "User",
                escapeHtml(message),
                LocalDateTime.now().format(DATE_FORMATTER)
        );
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;")
                .replace("\n", "<br>");
    }
}