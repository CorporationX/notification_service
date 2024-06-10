package faang.school.notificationservice.validator;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.DataNotificationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class EmailServiceValidatorTest {

    @InjectMocks
    private EmailServiceValidator emailServiceValidator;

    private UserDto userDto;
    private String message;
    private String path;

    @Test
    public void testCheckUserDtoIsNull() {
        assertThrows(DataNotificationException.class, () -> emailServiceValidator.checkUserDtoIsNull(userDto));
    }

    @Test
    public void testCheckMessageIsNull() {
        assertThrows(DataNotificationException.class, () -> emailServiceValidator.checkMessageIsNull(message));
    }

    @Test
    public void testCheckPathIsNull() {
        assertThrows(DataNotificationException.class, () -> emailServiceValidator.checkPathIsNull(path));
    }
}