package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.GoalCompletedEvent;
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

public class GoalCompletedEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MessageBuilder<GoalCompletedEvent> messageBuilder;

    @Mock
    private NotificationService notificationService;

    @Mock
    private Acknowledgment acknowledgment;

    @Mock
    private ConsumerRecord<String, String> consumerRecord;

    private GoalCompletedEventListener listener;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        List<MessageBuilder<GoalCompletedEvent>> builders = List.of(messageBuilder);
        List<NotificationService> notificationServices = List.of(notificationService);
        listener = new GoalCompletedEventListener(objectMapper, userServiceClient, builders, notificationServices);
    }

    @Test
    public void testOnMessage() {
        GoalCompletedEvent event = new GoalCompletedEvent(123L, 456L);
        String jsonValue = "{\"userId\":123,\"goalId\":456}";
        when(consumerRecord.value()).thenReturn(jsonValue);
        when(objectMapper.convertValue(jsonValue, GoalCompletedEvent.class)).thenReturn(event);
        when(messageBuilder.getInstance()).thenReturn((Class) GoalCompletedEvent.class);
        when(messageBuilder.buildMessage(event, Locale.UK)).thenReturn("Congratulations! You completed goal 456");

        UserDto userDto = UserDto.builder()
                .id(123L)
                .username("user123")
                .email("user123@example.com")
                .phone("1234567890")
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
        when(userServiceClient.getUser(123L)).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        listener.onMessage(consumerRecord, acknowledgment);

        verify(objectMapper).convertValue(jsonValue, GoalCompletedEvent.class);
        verify(messageBuilder).buildMessage(event, Locale.UK);
        verify(userServiceClient).getUser(123L);
        verify(notificationService).send(userDto, "Congratulations! You completed goal 456");
        verify(acknowledgment).acknowledge();
    }
}
