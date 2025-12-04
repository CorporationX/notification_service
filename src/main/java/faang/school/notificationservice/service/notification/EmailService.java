package faang.school.notificationservice.service.notification;

import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService{

    private final JavaMailSender mailSender;
    private static final String SUBJECT = "Goal has been achieved!";

    @Override
    public UserDto.PreferredContact getPreferredContact() {return UserDto.PreferredContact.EMAIL;}

    @Override
    public void send(UserDto user, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(SUBJECT);
        message.setText(text);
        mailSender.send(message);
    }
}