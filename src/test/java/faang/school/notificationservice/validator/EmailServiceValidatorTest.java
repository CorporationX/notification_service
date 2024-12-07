package faang.school.notificationservice.validator;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class EmailServiceValidatorTest {

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private EmailServiceValidator emailServiceValidator;

    @Test
    void validateUserDto_Success() {
        Mockito.lenient().when(userServiceClient.getUser(Mockito.anyLong())).thenReturn(new UserDto());
        assertDoesNotThrow(() -> emailServiceValidator.validateUserDto(new UserDto()));
    }

    @Test
    void validateUserDto_NotFound() {
        Mockito.lenient().when(userServiceClient.getUser(Mockito.anyLong())).thenThrow(FeignException.class);
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> emailServiceValidator.validateUserDto(new UserDto()));
        assertEquals("User with id %s not found".formatted(0L), exception.getMessage());
    }

    @Test
    void validateMessage_Success() {
        assertDoesNotThrow(() -> emailServiceValidator.validateMessage("example message"));
    }

    @Test
    void validateMessage_InvalidMessage() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> emailServiceValidator.validateMessage(null));
        assertEquals("Message cannot be null or blank", exception.getMessage());
    }
}