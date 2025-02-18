package faang.school.notificationservice.validator;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.DataValidationException;
import org.springframework.stereotype.Component;

@Component
public class NotificationValidator {
    public void validateNotification(UserDto user, String message) {
        if (user == null) {
            throw new DataValidationException("UserDto не может быть null");
        }
        if (message == null || message.trim().isEmpty()) {
            throw new DataValidationException("Сообщение не может быть пустым");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new DataValidationException("Email пользователя не может быть пустым");
        }
    }
}
