package faang.school.notificationservice.validation;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.InvalidUserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserValidatorTest {

    private final UserValidator validator = new UserValidator();

    @Test
    @DisplayName("Should throw InvalidUserException when user is null")
    void shouldThrowWhenUserIsNull() {
        InvalidUserException exception = assertThrows(InvalidUserException.class,
                () -> validator.validateForSms(null));

        assertEquals("User must not be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidUserException when phone is null")
    void shouldThrowWhenPhoneIsNull() {
        UserDto user = new UserDto();
        user.setPhone(null);

        InvalidUserException exception = assertThrows(InvalidUserException.class,
                () -> validator.validateForSms(user));

        assertEquals("Phone number is missing for SMS", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidUserException when phone is blank")
    void shouldThrowWhenPhoneIsBlank() {
        UserDto user = new UserDto();
        user.setPhone("   ");

        InvalidUserException exception = assertThrows(InvalidUserException.class,
                () -> validator.validateForSms(user));

        assertEquals("Phone number is missing for SMS", exception.getMessage());
    }

    @Test
    @DisplayName("Should pass validation when user and phone are valid")
    void shouldPassValidationWhenUserIsValid() {
        UserDto user = new UserDto();
        user.setPhone("+48123456789");

        assertDoesNotThrow(() -> validator.validateForSms(user));
    }
}
