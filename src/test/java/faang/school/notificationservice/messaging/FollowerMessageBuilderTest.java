package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerDto;
import faang.school.notificationservice.dto.SkillOfferDto;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowerMessageBuilderTest {
    @InjectMocks
    private FollowerMessageBuilder followerMessageBuilder;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private MessageSource messageSource;
    private UserDto userDto;
    private FollowerDto followerDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .username("User")
                .build();

        followerDto = FollowerDto.builder()
                .followeeId(1L)
                .followerId(2L)
                .build();
    }

    @Test
    void testGetInstance() {
        Class<?> instance = followerMessageBuilder.getInstance();
        assertEquals(FollowerDto.class, instance);
    }

    @Test
    void testBuildMessage() {
        when(userServiceClient.getUser(2L)).thenReturn(userDto);
        when(messageSource.getMessage(anyString(), any(Object[].class), any(Locale.class))).thenReturn("Test Message");

        String result = followerMessageBuilder.buildMessage(followerDto, Locale.getDefault());

        assertEquals("Test Message", result);
        verify(userServiceClient, times(1)).getUser(2L);
        verify(messageSource, times(1)).getMessage(anyString(), any(Object[].class), any(Locale.class));
    }

}