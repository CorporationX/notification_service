package faang.school.notificationservice.eventlistener;

import faang.school.notificationservice.dto.achievement.AchievementEvent;
import faang.school.notificationservice.listener.achievement.AchievementEventListener;
import faang.school.notificationservice.service.EmailService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private EmailService emailService;

    @Mock
    private MessageBuilder<AchievementEvent> messageBuilder;

    @Mock
    private Message message;

    private AchievementEventListener achievementEventListener;

    @BeforeEach
    void setUp() {
        achievementEventListener = new AchievementEventListener(
                objectMapper, userServiceClient, List.of(emailService), List.of(messageBuilder));
    }

    @Test
    void testOnMessage() throws IOException {
        AchievementEvent event = new AchievementEvent("copywriter", 2L);
        UserDto userReceiver = UserDto.builder().id(1L).build();
        userReceiver.setPreference(UserDto.PreferredContact.EMAIL);

        when(objectMapper.readValue(any(byte[].class), eq(AchievementEvent.class))).thenReturn(event);

        when(messageBuilder.buildMessage(event, Locale.getDefault()))
                .thenReturn("test message, achievement's title: " + event.title());

        when(messageBuilder.getInstance()).thenReturn(AchievementEvent.class);

        when(userServiceClient.getUser(event.userId())).thenReturn(userReceiver);

        when(emailService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        when(message.getBody()).thenReturn(new byte[0]);

        achievementEventListener.onMessage(message, new byte[0]);

        verify(emailService, times(1))
                .send(any(), eq("test message, achievement's title: " + event.title()));
    }
}
