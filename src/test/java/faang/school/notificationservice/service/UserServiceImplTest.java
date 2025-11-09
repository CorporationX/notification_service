package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.EntityNotFoundException;
import faang.school.notificationservice.exception.ServiceException;
import faang.school.notificationservice.service.user.UserServiceImpl;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private final long userId = 1L;
    private final UserDto userDto = UserDto.builder()
            .id(2L)
            .build();
    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testGetUserWithRetry() {
        when(userServiceClient.getUser(userDto.getId())).thenReturn(userDto);

        UserDto userWithRetry = userService.getUserWithRetry(userDto.getId());

        assertEquals(userDto.getId(), userWithRetry.getId());
    }

    @Test
    void testGetUserThrowsExceptionIfUserIsNull() {
        when(userServiceClient.getUser(userId)).thenReturn(null);

        getUserGeneralAsserts();
    }

    @Test
    void testGetUserThrowsExceptionIfUserIdIsNull() {
        when(userServiceClient.getUser(userId)).thenReturn(UserDto.builder().build());

        getUserGeneralAsserts();
    }

    @Test
    void testGetUserThrowsExceptionIfUserIdNotEqualsSetUserId() {
        when(userServiceClient.getUser(userId)).thenReturn(userDto);

        getUserGeneralAsserts();
    }

    @Test
    void testGetUserCatchFeignExceptionNotFound() {
        when(userServiceClient.getUser(userId)).thenThrow(FeignException.NotFound.class);

        getUserGeneralAsserts();
    }

    @Test
    void testGetUserCatchFeignException() {
        when(userServiceClient.getUser(userId)).thenThrow(FeignException.class);

        ServiceException serviceException = assertThrows(ServiceException.class,
                () -> userService.getUser(userId));

        assertEquals("User service unavailable", serviceException.getMessage());
    }

    @Test
    void testGetUserPositive() {
        when(userServiceClient.getUser(userDto.getId())).thenReturn(userDto);

        UserDto user = userService.getUser(userDto.getId());

        assertEquals(userDto.getId(), user.getId());
    }

    private void getUserGeneralAsserts() {
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> userService.getUser(userId));

        assertEquals("User %d not found".formatted(userId), entityNotFoundException.getMessage());
    }
}