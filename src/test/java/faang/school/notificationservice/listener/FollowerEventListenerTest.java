package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.mapper.JsonObjectMapper;
import faang.school.notificationservice.message.FollowEventMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.TelegramNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.util.List;
import java.util.Locale;

@ExtendWith(MockitoExtension.class)
class FollowerEventListenerTest {

    @InjectMocks
    FollowerEventListener followerEventListener;
    @Mock
    JsonObjectMapper jsonObjectMapper;
    @Spy
    List<NotificationService> notificationServiceList;
    @Mock
    UserServiceClient userServiceClient;
    @Mock
    FollowEventMessageBuilder followEventMessageBuilder;
    @Mock
    TelegramNotificationService telegramNotificationService;
    @Mock
    Message redisMessage;

    @BeforeEach
    void setUp() {
        notificationServiceList = List.of(telegramNotificationService);
        followerEventListener = new FollowerEventListener(jsonObjectMapper, notificationServiceList,
                userServiceClient, followEventMessageBuilder);
    }

    @Test
    void testFollowerEventListener() {
        FollowerEventDto followerEventDto = FollowerEventDto.builder().followerId(1L).build();
        Locale locale = new Locale("ENGLISH");
        UserDto userDto = UserDto.builder()
                .preference(UserDto.PreferredContact.TELEGRAM)
                .locale(locale)
                .username("User")
                .build();
        String messageText = "message";
        byte[] message = messageText.getBytes();

        Mockito.when(jsonObjectMapper.readValue(redisMessage.getBody(), FollowerEventDto.class)).thenReturn(followerEventDto);
        Mockito.when(userServiceClient.getUser(followerEventDto.getFollowerId())).thenReturn(userDto);
        Mockito.when(followEventMessageBuilder.buildMessage(locale, userDto.getUsername())).thenReturn(messageText);
        Mockito.when(telegramNotificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.TELEGRAM);

        followerEventListener.onMessage(redisMessage, message);
        Mockito.verify(telegramNotificationService).send(userDto, messageText);
    }
}