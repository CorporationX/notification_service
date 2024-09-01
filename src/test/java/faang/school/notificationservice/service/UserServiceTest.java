package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.ExceptionMessages;
import faang.school.notificationservice.exception.feign.UserServiceException;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserServiceClient userServiceClient;
    private UserDto userDto;
    private Long userId;

    @BeforeEach
    void setUp() {
        userId = 1L;
        userDto = new UserDto();
        userDto.setId(userId);
        userDto.setUsername("testUser");
    }

    @Test
    void fetchUserReturnsUserDtoForValidId() {
        when(userServiceClient.getUser(userId)).thenReturn(userDto);

        UserDto result = userService.fetchUser(userId);

        assertEquals(userDto, result);
    }

    @Test
    void fetchUserThrowsUserServiceExceptionOnFeignException() {
        when(userServiceClient.getUser(userId)).thenThrow(FeignException.class);

        UserServiceException exception = assertThrows(UserServiceException.class, () -> userService.fetchUser(userId));
        assertEquals(String.format(ExceptionMessages.USER_DATA_FETCH_FAILURE, userId), exception.getMessage());
    }

    @Test
    void fetchUserThrowsUserServiceExceptionOnRestClientException() {
        when(userServiceClient.getUser(userId)).thenThrow(RestClientException.class);

        UserServiceException exception = assertThrows(UserServiceException.class, () -> userService.fetchUser(userId));
        assertEquals(String.format(ExceptionMessages.USER_DATA_FETCH_FAILURE, userId), exception.getMessage());
    }
}
