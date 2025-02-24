package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationRequestedEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecommendationEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MessageBuilder<RecommendationRequestedEvent> messageBuilder;

    @Mock
    private NotificationService notificationService;

    @Mock
    private Acknowledgment acknowledgment;

    @Mock
    private ConsumerRecord<String, String> consumerRecord;

    private RecommendationEventListener listener;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        List<MessageBuilder<RecommendationRequestedEvent>> builders = List.of(messageBuilder);
        List<NotificationService> notificationServices = List.of(notificationService);
        listener = new RecommendationEventListener(objectMapper, userServiceClient, builders, notificationServices);
    }

    @Test
    public void testOnMessage() {
        // Создаем тестовое событие
        RecommendationRequestedEvent event = RecommendationRequestedEvent.builder()
                .requestAuthorId(111L)
                .targetUserId(222L)
                .recommendationRequestId(333L)
                .build();

        // JSON-представление события
        String jsonValue = "{\"requestAuthorId\":111,\"targetUserId\":222,\"recommendationRequestId\":333}";
        when(consumerRecord.value()).thenReturn(jsonValue);
        when(objectMapper.convertValue(jsonValue, RecommendationRequestedEvent.class)).thenReturn(event);
        when(messageBuilder.getInstance()).thenReturn((Class) RecommendationRequestedEvent.class);
        when(messageBuilder.buildMessage(event, Locale.getDefault())).thenReturn("User 111 requested recommendation, request id 333");

        UserDto userDto = UserDto.builder()
                .id(222L)
                .username("user222")
                .email("user222@example.com")
                .phone("5555555555")
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
        when(userServiceClient.getUser(222L)).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        listener.onMessage(consumerRecord, acknowledgment);

        verify(objectMapper).convertValue(jsonValue, RecommendationRequestedEvent.class);
        verify(messageBuilder).buildMessage(event, Locale.getDefault());
        verify(userServiceClient).getUser(222L);
        verify(notificationService).send(userDto, "User 111 requested recommendation, request id 333");
        verify(acknowledgment).acknowledge();
    }
}
