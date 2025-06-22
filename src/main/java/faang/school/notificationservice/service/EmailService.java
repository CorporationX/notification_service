package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService {

    private final JavaMailSender javaMailSender;

    @Override
    public void send(UserDto user, String message) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom("uralostrov6@gmail.com");
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Костя черт!");
        mailMessage.setText(message + " " + user.getUsername());

        javaMailSender.send(mailMessage);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}