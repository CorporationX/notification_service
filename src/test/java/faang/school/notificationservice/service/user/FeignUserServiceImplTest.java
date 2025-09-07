package faang.school.notificationservice.service.user;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.EntityNotFoundException;
import faang.school.notificationservice.exception.ServiceUnavailableException;
import feign.FeignException;
import feign.RetryableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FeignUserServiceImplTest {

    private static final long USER_ID = 1L;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private FeignException genericFeignException;

    @Mock
    private FeignException.NotFound feignNotFound;

    @Mock
    private RetryableException retryableException;

    private FeignUserServiceImpl service;

    @BeforeEach
    public void setUp() {
        service = new FeignUserServiceImpl(userServiceClient);
    }

    @Test
    @DisplayName("Should return UserDto when found")
    public void shouldReturnUserDtoWhenFound() {
        UserDto dto = new UserDto();
        dto.setId(USER_ID);
        when(userServiceClient.getUser(USER_ID)).thenReturn(dto);

        UserDto result = service.getById(USER_ID);

        assertEquals(dto, result);
        verify(userServiceClient).getUser(USER_ID);
        verifyNoMoreInteractions(userServiceClient);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when user is null")
    public void shouldThrowWhenUserIsNull() {
        when(userServiceClient.getUser(USER_ID)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> service.getById(USER_ID));
        verify(userServiceClient).getUser(USER_ID);
        verifyNoMoreInteractions(userServiceClient);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when FeignException.NotFound is thrown by client")
    public void shouldThrowWhenFeignNotFound() {
        when(userServiceClient.getUser(USER_ID)).thenThrow(feignNotFound);

        assertThrows(EntityNotFoundException.class, () -> service.getById(USER_ID));
        verify(userServiceClient).getUser(USER_ID);
        verifyNoMoreInteractions(userServiceClient);
    }

    @Test
    @DisplayName("Should throw ServiceUnavailableException when RetryableException is thrown by client")
    public void shouldThrowWhenRetryable() {
        when(userServiceClient.getUser(USER_ID)).thenThrow(retryableException);

        assertThrows(ServiceUnavailableException.class, () -> service.getById(USER_ID));
        verify(userServiceClient).getUser(USER_ID);
        verifyNoMoreInteractions(userServiceClient);
    }

    @Test
    @DisplayName("Should rethrow FeignException for non-handled cases")
    public void shouldRethrowFeignException() {
        when(userServiceClient.getUser(USER_ID)).thenThrow(genericFeignException);

        assertThrows(FeignException.class, () -> service.getById(USER_ID));
        verify(userServiceClient).getUser(USER_ID);
        verifyNoMoreInteractions(userServiceClient);
    }
}
