package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements NotificationService {
    private final JavaMailSender emailMailSender;
    @Value("${spring.mail.messages.basename}")
    private String subject;
    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
    @Override
    public void send(UserDto user, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("CorpX");
        message.setTo("bugsi-47@yandex.ru");
        message.setSubject(subject);
        message.setText(text);
        emailMailSender.send(message);
        log.info("Email sent to {}", user.getEmail());
    }
}
