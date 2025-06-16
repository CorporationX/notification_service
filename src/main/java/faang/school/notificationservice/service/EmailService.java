package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailService implements NotificationService{

    @Autowired
    private JavaMailSender emailSender;

    @Value("spring.mail.username")
    private String emailAddress;

    @Override
    public void send(UserDto user, String message) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(emailAddress);
        msg.setTo(user.getEmail());
        msg.setSubject("Notification");
        msg.setText(message);
        emailSender.send(msg);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
