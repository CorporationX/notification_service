package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.validator.EmailServiceValidator;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService {

    private final JavaMailSender mailSender;

    private final EmailServiceValidator emailServiceValidator;

    @Override
    @Async("mailExecutor")
    public void send(UserDto user, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        emailServiceValidator.validateUserDto(user);
        emailServiceValidator.validateMessage(message);

        mailMessage.setTo(user.getEmail());
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
