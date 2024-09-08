package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.AchievementEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.AchievementMessageBuilder;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementEventListenerTest {
    @InjectMocks
    private AchievementEventListener achievementEventListener;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private NotificationService notificationService;
    @Mock
    private AchievementMessageBuilder achievementMessageBuilder;
    @Mock
    private Message message;

    @BeforeEach
    void setUp() {
        List<MessageBuilder<AchievementEvent>> messageBuilderList
                = List.of(achievementMessageBuilder);
        List<NotificationService> notificationServiceList = List.of(notificationService);

        achievementEventListener = new AchievementEventListener(
                objectMapper,
                userServiceClient,
                messageBuilderList,
                notificationServiceList);
    }

    @Test
    void shouldProcessAchievementEventAndSendNotification() throws IOException {
        // given
        var userLocale = Locale.GERMANY;
        AchievementEvent achievementEvent = new AchievementEvent();
        achievementEvent.setUserId(23L);

        var preferredContact = UserDto.PreferredContact.EMAIL;
        UserDto userDto = UserDto.builder()
                .id(1L)
                .preference(preferredContact)
                .build();
        var achievementMessage = "Herzlichen Glückwunsch! Du hast neue Errungenschaften erreicht{0}.";

        when(objectMapper.readValue(any(byte[].class), eq(AchievementEvent.class)))
                .thenReturn(achievementEvent);
        when(userServiceClient.getUser(anyLong()))
                .thenReturn(userDto);
        when(notificationService.getPreferredContact())
                .thenReturn(preferredContact);
        when(message.getBody()).thenReturn(new byte[0]);
        when(achievementMessageBuilder.buildMessage(eq(achievementEvent), eq(userLocale)))
                .thenReturn(achievementMessage);

        when(achievementMessageBuilder.supportsEventType()).thenReturn(AchievementEvent.class);

        // when
        achievementEventListener.onMessage(message, null);

        // then
        verify(objectMapper).readValue(any(byte[].class), eq(AchievementEvent.class));
        verify(userServiceClient).getUser(anyLong());
        verify(notificationService).send(eq(userDto), eq(achievementMessage));
    }
}