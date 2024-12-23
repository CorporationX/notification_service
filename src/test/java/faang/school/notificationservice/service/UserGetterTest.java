package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserGetterTest {
    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private UserGetter userGetter;

    private final long userId = 1L;


    @Test
    public void getUserWithValidateWithStatus400FailTest() {
        when(userServiceClient.getUser(userId)).thenThrow(new FeignException(400, "message") {
        });

        EntityNotFoundException entityNotFoundException =
                assertThrows(EntityNotFoundException.class,
                        () -> userGetter.getUserWithValidate(userId)
                );

        verify(userServiceClient, times(1)).getUser(userId);
        assertTrue(entityNotFoundException.getMessage().contains(String.format(UserGetter.USER_NOT_FOUND_BY_ID, userId, "")));
    }

    @Test
    public void getUserWithValidateWithStatus500FailTest() {
        when(userServiceClient.getUser(userId)).thenThrow(new FeignException(500, "message") {
        });

        RuntimeException runtimeException =
                assertThrows(RuntimeException.class,
                        () -> userGetter.getUserWithValidate(userId)

                );

        verify(userServiceClient, times(1)).getUser(userId);
        assertTrue(runtimeException.getMessage().contains(String.format(UserGetter.INTERNAL_SERVER_ERROR, userId, "")));
    }

    @Test
    public void getUserWithValidateWithOtherStatusFailTest() {
        when(userServiceClient.getUser(userId)).thenThrow(new FeignException(503, "message") {
        });

        RuntimeException runtimeException =
                assertThrows(RuntimeException.class,
                        () -> userGetter.getUserWithValidate(userId)

                );

        verify(userServiceClient, times(1)).getUser(userId);
        assertTrue(runtimeException.getMessage().contains(String.format(UserGetter.INTERNAL_SERVER_ERROR, userId, HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase())));
    }

    @Test
    public void getUserWithValidateSuccessTest() {
        when(userServiceClient.getUser(userId)).thenReturn(any());

        userGetter.getUserWithValidate(userId);

        verify(userServiceClient, times(1)).getUser(userId);
    }
}
