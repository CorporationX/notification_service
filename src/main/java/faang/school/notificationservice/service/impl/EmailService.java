package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.enums.PreferredContact;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService {

    private final JavaMailSender javaMailSender;

    @Override
    public void send(UserDto user, String text) {
        log.info("Sending email notification for user " + user.getUsername() + ", message: " + text);

//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(user.getEmail());
//        message.setSubject("Notification from FAANG School");
//        message.setText(text);
//        message.setFrom("your_mail@gmail.com");
//        javaMailSender.send(message);

        System.out.println(text);
        log.info("Success email notification for user " + user.getUsername());
    }

    @Override
    public PreferredContact getPreferredContact() {
        return PreferredContact.EMAIL;
    }
}