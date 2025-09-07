package faang.school.notificationservice.validation;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.InvalidUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserSmsValidator {

    private static final String PHONE_REGEX = "^\\+?[1-9]\\d{1,14}$";

    public void validateForSms(UserDto user) {
        if (user == null) {
            log.warn("SMS validation failed: user is null");
            throw new InvalidUserException("User must not be null");
        }

        String phone = user.getPhone();

        if (phone == null || phone.isBlank()) {
            log.warn("SMS validation failed: phone number is missing for user: {}", user.getId());
            throw new InvalidUserException("Phone number is missing for SMS");
        }

        if (!phone.matches(PHONE_REGEX)) {
            log.warn("SMS validation failed: invalid phone number format for user {}: {}", user.getId(), phone);
            throw new InvalidUserException("Invalid phone number format: " + phone);
        }
    }
}
