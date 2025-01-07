package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.RetryProperties;
import faang.school.notificationservice.dto.UserContactsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserFeignServiceTest {

    @Mock
    private RetryProperties retryProperties;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private UserFeignService userFeignService;

    private Long userId;
    private UserContactsDto userContactsDto;

    @BeforeEach
    void setUp() {
        userId = 1L;
        userContactsDto = UserContactsDto.builder().id(userId).build();
    }

    @Test
    void testGetUserContactsSuccess() {
        when(userServiceClient.getUserContacts(userId)).thenReturn(userContactsDto);

        UserContactsDto result = userFeignService.getUserContacts(userId);

        verify(userServiceClient, times(1)).getUserContacts(userId);
        assertThat(result).isEqualTo(userContactsDto);
    }

    @Test
    void testGetUserContactsShouldThrowException() {
        when(userServiceClient.getUserContacts(userId)).thenThrow(new RuntimeException("User service unavailable"));

        assertThatThrownBy(() -> userFeignService.getUserContacts(userId)).hasMessageContaining("User service unavailable");
    }
}