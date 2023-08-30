package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Data
@Slf4j
public class EmailService implements NotificationService{

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.default_subject}")
    private String subject;

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }

    @Override
    public void send(UserDto userDto, String messageText) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("noreply@faang-school.com");
        message.setTo(userDto.getEmail());
        message.setSubject(subject);
        message.setText(messageText);

        javaMailSender.send(message);

        log.info("Email message send to email address: {}", userDto.getEmail());
    }
}
