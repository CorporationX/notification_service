package faang.school.notificationservice.handler;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserServiceDto;
import faang.school.notificationservice.exception.impl.retryable.UserServiceClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceHandlerTest {

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private UserServiceHandler userServiceHandler;

    private UserServiceDto userServiceDto;
    private Long userId;
    private List<Long> userIds;

    @BeforeEach
    void setUp() {
        userServiceDto = new UserServiceDto();
        userId = 1L;
        userIds = List.of(1L, 2L, 3L);
    }

    @Test
    void getSingleUserSuccess() {
        when(userServiceClient.getUser(userId)).thenReturn(userServiceDto);

        UserServiceDto result = userServiceHandler.getSingleUser(userId);

        assertEquals(userServiceDto, result);
        verify(userServiceClient).getUser(userId);
    }

    @Test
    void getSingleUserUserServiceClientException() {
        when(userServiceClient.getUser(userId)).thenThrow(new RuntimeException("Test exception"));

        assertThrows(UserServiceClientException.class, () -> userServiceHandler.getSingleUser(userId));
        verify(userServiceClient).getUser(userId);
    }

    @Test
    void getUserListSuccessReturnsListOfUserServiceDto() {
        List<UserServiceDto> userServiceDtoList = List.of(userServiceDto);
        when(userServiceClient.getUsers(userIds)).thenReturn(userServiceDtoList);

        List<UserServiceDto> result = userServiceHandler.getUserList(userIds);

        assertEquals(userServiceDtoList, result);
        verify(userServiceClient).getUsers(userIds);
    }

    @Test
    void getUserListUserServiceClientException() {
        when(userServiceClient.getUsers(userIds)).thenThrow(new RuntimeException("Test exception"));

        assertThrows(UserServiceClientException.class, () -> userServiceHandler.getUserList(userIds));
        verify(userServiceClient).getUsers(userIds);
    }

    @Test
    void getUsersByIdsInGivenOrderSuccess() {
        List<UserServiceDto> userServiceDtoList = List.of(userServiceDto);
        when(userServiceClient.getUsersByIdsInGivenOrder(userIds)).thenReturn(userServiceDtoList);

        List<UserServiceDto> result = userServiceHandler.getUsersByIdsInGivenOrder(userIds);

        assertEquals(userServiceDtoList, result);
        verify(userServiceClient).getUsersByIdsInGivenOrder(userIds);
    }

    @Test
    void getUsersByIdsInGivenOrderUserServiceClientException() {
        when(userServiceClient.getUsersByIdsInGivenOrder(userIds)).thenThrow(new RuntimeException("Test exception"));

        assertThrows(UserServiceClientException.class, () -> userServiceHandler.getUsersByIdsInGivenOrder(userIds));
        verify(userServiceClient).getUsersByIdsInGivenOrder(userIds);
    }
}