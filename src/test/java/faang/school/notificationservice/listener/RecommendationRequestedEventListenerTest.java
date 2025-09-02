package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationRequestedEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестирование класса-наследника {@link RecommendationRequestedEventListener}
 *
 * @author Linempy
 * @since 15.08.2025
 */
@DisplayName("Тестирование RecommendationRequestedEventListener")
@ExtendWith(MockitoExtension.class)
public class RecommendationRequestedEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userClient;

    private final List<NotificationService> services = new ArrayList<>();
    private final List<MessageBuilder<RecommendationRequestedEvent>> builders = new ArrayList<>();

    @InjectMocks
    private RecommendationRequestedEventListener listener;

    @Mock
    private Message message;

    @Mock
    private MessageBuilder<RecommendationRequestedEvent> messageBuilder;

    @Mock
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        builders.add(messageBuilder);
        services.add(notificationService);

        listener = new RecommendationRequestedEventListener(
                objectMapper,
                userClient,
                services,
                builders
        );
    }

    @Test
    @DisplayName("При успешном событии рекомендации должно отправляться уведомление")
    public void onMessage_handleRecommendationEvent_Success() throws IOException {
        RecommendationRequestedEvent event = new RecommendationRequestedEvent(1L, 2L, 1L);
        String jsonEvent = "{\"requesterId\": 1, \"receiverId\": 2, \"requestId\": 1}";
        UserDto userDto = new UserDto();
        userDto.setPhone("+131283128");
        userDto.setUsername("Steve");
        userDto.setPreference(UserDto.PreferredContact.PHONE);

        String testMessage = "Тестовое сообщение";

        when(message.getBody()).thenReturn(jsonEvent.getBytes());
        when(objectMapper.readValue(jsonEvent.getBytes(), RecommendationRequestedEvent.class)).thenReturn(event);
        when(messageBuilder.getInstance()).thenAnswer(invocation -> RecommendationRequestedEvent.class);
        when(messageBuilder.buildMessage(eq(event), any(Locale.class))).thenReturn(testMessage);
        when(userClient.getUser(event.receiverId()))
                .thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.PHONE);

        listener.onMessage(message, "pattern".getBytes());

        verify(objectMapper, times(1))
                .readValue(jsonEvent.getBytes(), RecommendationRequestedEvent.class);
        verify(userClient).getUser(event.receiverId());
        verify(messageBuilder, times(1)).getInstance();
        verify(notificationService, times(1)).getPreferredContact();
        verify(notificationService, times(1)).send(userDto, testMessage);
    }
}