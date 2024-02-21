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
    private final JavaMailSender javaMailSender;
    private final SimpleMailMessage message;
    @Value("$(mail.messages.basename)")
    private String subject;

    @Override
    public void send(UserDto user, String text) {
        message.setTo(user.getEmail());
        message.setText(text);
        message.setSubject(subject);

        javaMailSender.send(message);
        log.info("Email sent to {}", user.getEmail());
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
