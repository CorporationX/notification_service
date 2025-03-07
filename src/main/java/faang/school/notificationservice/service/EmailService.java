package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserServiceDto;
import faang.school.notificationservice.exception.SendNotificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService implements NotificationService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sendFrom;

    @Value("${mail-settings.subject}")
    private String subject;

    @Override
    public void send(UserServiceDto userDto, String message) {
        SimpleMailMessage emailBody = new SimpleMailMessage();

        String userEmail = validateDto(userDto);

        emailBody.setFrom(sendFrom);
        emailBody.setTo(userEmail);
        emailBody.setSubject(subject);
        emailBody.setText(message);

        try {
            mailSender.send(emailBody);
            log.info("Sent email to {}", userEmail);
        } catch (MailException exception) {
            log.error("Error with email sending to {} due the {}", userEmail, exception.getMessage());
            throw new SendNotificationException(String.format("Email to %s didn't send", userEmail));
        }
    }

    @Override
    public UserServiceDto.PreferredContact getPreferredContact() {
        return UserServiceDto.PreferredContact.EMAIL;
    }

    private String validateDto(UserServiceDto dto) {
        if (dto.getPreference() != getPreferredContact()) {
            throw new NoSuchElementException(
                    String.format("User %s didn't set email notification option", dto.getUsername()));
        }

        String userEmail = dto.getEmail();

        if (!EmailValidator.getInstance().isValid(userEmail)) {
            throw new SendNotificationException(String.format("Email %s is invalid", userEmail));
        }

        return userEmail;
    }
}
