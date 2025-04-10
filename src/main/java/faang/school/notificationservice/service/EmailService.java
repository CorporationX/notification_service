package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.NotificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Сервис для отправки уведомлений по электронной почте.
 * Реализует интерфейс NotificationService.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService {

    private final JavaMailSender mailSender;

    /**
     * Отправляет уведомление пользователю по электронной почте.
     *
     * @param user пользователь, которому отправляется уведомление
     * @param text текст уведомления
     */
    @Override
    public void send(UserDto user, String text) {
        SimpleMailMessage emailMessage = new SimpleMailMessage();

        emailMessage.setTo(user.getEmail());
        emailMessage.setSubject("Notification");
        emailMessage.setText(text);

        try {
            mailSender.send(emailMessage);
            log.info("Email sent to {}", user.getEmail());
        } catch (Exception exception) {
            log.error("Failed to send email to {}", user.getEmail(), exception);
            throw new NotificationException("Failed to send email");
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}