package faang.school.notificationservice.validation;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.InvalidUserException;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public void validateForSms(UserDto user) {
        if (user == null) {
            throw new InvalidUserException("User must not be null");
        }
        if (user.getPhone() == null || user.getPhone().isBlank()) {
            throw new InvalidUserException("Phone number is missing for SMS");
        }
    }
}
