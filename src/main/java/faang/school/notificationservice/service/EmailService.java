package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService implements NotificationService {

    private final JavaMailSender mailSender;

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }

    @Override
    public void send(UserDto userDto, String text) {
        if (userDto.getPreferredContact() == null) {
            log.warn("Предпочтение пользователя не указано, используется значение по умолчанию: EMAIL");
            userDto.setPreference(UserDto.PreferredContact.EMAIL);
        }
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(userDto.getEmail());
            mailMessage.setSubject("Новое уведомление от NotificationService");
            mailMessage.setText(String.format("Здравствуйте, %s!\n\n%s", userDto.getUsername(), text));

            mailSender.send(mailMessage);
            log.info("Письмо успешно отправлено на адрес: {}", userDto.getEmail());
        } catch (MailException e) {
            log.error("Ошибка при отправке письма на адрес {}: {}", userDto.getEmail(), e.getMessage());
        }
    }
}
