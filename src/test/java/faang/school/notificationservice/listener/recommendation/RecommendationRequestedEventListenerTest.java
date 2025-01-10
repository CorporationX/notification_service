package faang.school.notificationservice.listener.recommendation;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.recommendation.RecommendationRequestedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationRequestedEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private NotificationService notificationService;
    @Mock
    private MessageBuilder<RecommendationRequestedEvent> messageBuilder;

    private RecommendationRequestedEventListener requestedEventListener;

    @BeforeEach
    void setUp() {
        requestedEventListener = new RecommendationRequestedEventListener(objectMapper,
                userServiceClient,
                List.of(notificationService),
                List.of(messageBuilder));
    }

    @Test
    void testOnMessage_Successful() throws IOException {
        long userId = 1L;
        RecommendationRequestedEvent requestedEvent = RecommendationRequestedEvent.builder()
                .receiverId(userId)
                .build();
        Message message = mock(Message.class);
        UserDto userDto = UserDto.builder()
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
        String text = "test message";

        when(objectMapper.readValue(message.getBody(), RecommendationRequestedEvent.class)).thenReturn(requestedEvent);
        when(messageBuilder.getInstance()).thenReturn(RecommendationRequestedEvent.class);
        when(messageBuilder.buildMessage(requestedEvent, Locale.getDefault())).thenReturn(text);
        when(userServiceClient.getUser(userId)).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        requestedEventListener.onMessage(message, null);

        verify(notificationService).send(userDto, text);
    }
}