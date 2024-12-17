package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.data.NotificationChannel;
import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.event.LikeEvent;
import faang.school.notificationservice.messaging.LikeMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.UserFeignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.connection.Message;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikeEventListenerTest {
    @Mock
    private LikeMessageBuilder likeMessageBuilder;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserFeignService userFeignService;

    @Mock
    private NotificationService emailNotificationService;

    @InjectMocks
    private LikeEventListener likeEventListener;

    @BeforeEach
    void setUp() {
        likeEventListener = new LikeEventListener(
                likeMessageBuilder,
                objectMapper,
                userFeignService,
                List.of(emailNotificationService)
        );
    }

    @Test
    void onMessageShouldProcessMessageAndSendNotification() throws Exception {
        String expectedMessage = "You've received a new like!";
        LikeEvent event = LikeEvent.builder().likeAuthorId(1L).postAuthorId(1L).postId(1L).build();

        UserContactsDto userContactsDto = new UserContactsDto();
        userContactsDto.setId(1L);
        userContactsDto.setPreference(NotificationChannel.EMAIL);

        Message redisMessage = mock(Message.class);
        byte[] body = "{\"likeId\":1,\"postId\":1,\"postAuthorId\":1}".getBytes(StandardCharsets.UTF_8);
        when(redisMessage.getBody()).thenReturn(body);

        when(objectMapper.readValue(eq(body), eq(LikeEvent.class))).thenReturn(event);

        when(userFeignService.getUserContacts(1L)).thenReturn(userContactsDto);
        when(emailNotificationService.getPreferredContact()).thenReturn(NotificationChannel.EMAIL);
        when(likeMessageBuilder.buildMessage(event, LocaleContextHolder.getLocale())).thenReturn(expectedMessage);

        likeEventListener.onMessage(redisMessage, null);

        ArgumentCaptor<UserContactsDto> userCaptor = ArgumentCaptor.forClass(UserContactsDto.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        verify(emailNotificationService, times(1)).send(userCaptor.capture(), messageCaptor.capture());

        assertEquals(1L, userCaptor.getValue().getId());
        assertEquals(expectedMessage, messageCaptor.getValue());
    }
}