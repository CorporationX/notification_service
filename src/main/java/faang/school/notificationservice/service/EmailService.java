package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.InvalidUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService {

    private final JavaMailSender mailSender;

    @Override
    public void send(UserDto userDto, String message) {
        validateUserDto(userDto);
        log.info("Валидация пользователя прошла успешно");
        userDto.setPreference(getPreferredContact());
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(userDto.getEmail());
        simpleMailMessage.setText(message);
        mailSender.send(simpleMailMessage);
        log.info("Email отправлен пользователю {} с id {}", userDto.getUsername(), userDto.getId());
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }

    private void validateUserDto(UserDto userDto) {
        if (userDto == null) {
            log.error("Пользователь равен null");
            throw new InvalidUserException("Пользователя не найден");
        } else if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new NullPointerException("Email пользователя не может быть пустым");
        }

    }
}
