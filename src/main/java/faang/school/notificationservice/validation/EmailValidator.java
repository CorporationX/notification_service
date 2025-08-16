package faang.school.notificationservice.validation;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.EmailValidationException;
import faang.school.notificationservice.exception.InvalidUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailValidator {
    private final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public void validateForEmail(UserDto user) {
        if (user == null) {
            log.warn("Email validation failed: user is null");
            throw new InvalidUserException("User is null");
        }

        if (user.getEmail().isBlank()) {
            log.warn("Email validation failed: invalid format");
            throw new EmailValidationException("Email is null or Blank");
        }

        if (!user.getEmail().matches(EMAIL_REGEX)) {
            log.warn("Email validation failed: invalid format");
            throw new EmailValidationException("Invalid email format");
        }
    }
}
