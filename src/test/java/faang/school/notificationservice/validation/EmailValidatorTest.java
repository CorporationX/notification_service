package faang.school.notificationservice.validation;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.EmailValidationException;
import faang.school.notificationservice.exception.InvalidUserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class EmailValidatorTest {

    private final EmailValidator emailValidator = new EmailValidator();

    @Test
    @DisplayName("Should throw InvalidUserException when user is null")
    public void throwWhenUserIsNull() {
        assertThrows(InvalidUserException.class, () -> emailValidator.validateForEmail(null));
    }

    @Test
    @DisplayName("Should throw EmailValidationException when email is blank")
    public void throwWhenEmailIsBlank() {
        UserDto userDto = createUserDto("");

        EmailValidationException e = assertThrows(EmailValidationException.class,
                () -> emailValidator.validateForEmail(userDto));
        assertEquals("Email is null or Blank", e.getMessage());
    }

    @Test
    @DisplayName("Should throw EmailValidationException when email is blank")
    public void throwWhenEmailInvalidFormat() {
        UserDto userDto = createUserDto("123mail.com");

        EmailValidationException e = assertThrows(EmailValidationException.class,
                () -> emailValidator.validateForEmail(userDto));
        assertEquals("Invalid email format", e.getMessage());
    }

    @Test
    @DisplayName("Should pass validation when email and user is valid")
    public void passValidation() {
        UserDto userDto = createUserDto("123@mail.com");

        assertDoesNotThrow(() -> emailValidator.validateForEmail(userDto));
    }

    private UserDto createUserDto(String email) {
        UserDto userDto = new UserDto();
        userDto.setEmail(email);

        return userDto;
    }
}
