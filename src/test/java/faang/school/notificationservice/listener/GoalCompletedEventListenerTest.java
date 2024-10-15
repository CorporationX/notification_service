package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.messaging.GoalCompletedEventMessageBuilder;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.GoalCompletedEvent;
import faang.school.notificationservice.service.telegram.TelegramService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoalCompletedEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private GoalCompletedEventMessageBuilder messageBuilder;

    @Mock
    private Message message;

    @Mock
    private TelegramService telegramService;

    @InjectMocks
    private GoalCompletedEventListener listener;

    private GoalCompletedEvent event;
    private UserDto user;
    private final Locale locale = Locale.getDefault();
    private String jsonProjectFollowerEvent;

    @BeforeEach
    void setup() {
        jsonProjectFollowerEvent = "{ \"userId\": 1, \"goalId\": 2, \"completedAt\": \"2024-10-01T15:00:00\" }";
        user = UserDto.builder().id(1L).preference(UserDto.PreferredContact.TELEGRAM).build();
        listener = new GoalCompletedEventListener(objectMapper, userServiceClient, messageBuilder, List.of(telegramService));

        event = GoalCompletedEvent.builder()
                .userId(1)
                .goalId(2)
                .completedAt(LocalDateTime.parse("2024-10-01T15:00:00"))
                .build();
    }

    @Test
    void testOnMessage() throws IOException {
        when(message.getBody()).thenReturn(jsonProjectFollowerEvent.getBytes());
        when(objectMapper.readValue(jsonProjectFollowerEvent.getBytes(), GoalCompletedEvent.class)).thenReturn(event);
        when(messageBuilder.buildMessage(event, locale)).thenReturn("You have reached a goal");
        when(userServiceClient.getUser(anyLong())).thenReturn(user);
        when(telegramService.getPreferredContact()).thenReturn(UserDto.PreferredContact.TELEGRAM);

        listener.onMessage(message, new byte[0]);

        verify(objectMapper).readValue(message.getBody(), GoalCompletedEvent.class);
        verify(messageBuilder).buildMessage(event, locale);
    }
}