package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationService implements NotificationService {
    private final JavaMailSender emailSender;
    private final Configuration freemarkerConfig;

    @Value("${spring.mail.default-subject}")
    private String subject;

    @Value("${spring.mail.template-name}")
    private String templateName;

    @Override
    public void send(UserDto user, String message) {
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("username", user.getUsername());
            model.put("message", message);

            Template template = freemarkerConfig.getTemplate(templateName);
            StringWriter stringWriter = new StringWriter();
            template.process(model, stringWriter);
            String htmlBody = stringWriter.toString();


            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            emailSender.send(mimeMessage);
            log.info("EmailNotificationService: successfully sent email to user {}", user.getUsername());
        } catch (MessagingException | IOException | TemplateException e) {
            log.error("EmailNotificationService: Failed to send email notification to user {}",
                    user.getUsername(), e);
            throw new RuntimeException("Failed to send email notification", e);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
