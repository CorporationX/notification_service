package faang.school.notificationservice.eventlistener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.recommendation.RecommendationReceivedEvent;
import faang.school.notificationservice.eventlistener.recommendation.RecommendationReceivedEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecommendationReceivedEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private NotificationService notificationService;

    private RecommendationReceivedEventListener eventListener;
    private List<NotificationService> notificationServices;
    private List<MessageBuilder<RecommendationReceivedEvent>> messageBuilders;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        messageBuilders = new ArrayList<>();
        MessageBuilder<RecommendationReceivedEvent> mockedMessageBuilder = mock(MessageBuilder.class);
        when(mockedMessageBuilder.getInstance()).thenReturn(RecommendationReceivedEvent.class);
        messageBuilders.add(mockedMessageBuilder);

        notificationServices = new ArrayList<>();
        notificationServices.add(notificationService);

        eventListener = new RecommendationReceivedEventListener(objectMapper,
                userServiceClient,
                notificationServices,
                messageBuilders);
    }

    @Test
    public void testOnMessageSuccess() throws Exception {
        RecommendationReceivedEvent event = prepareEvent();
        UserDto user = prepareUser();

        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(message.getBody()).thenReturn(messageBody);

        when(userServiceClient.getUser(3L)).thenReturn(user);
        when(objectMapper.readValue(messageBody, RecommendationReceivedEvent.class)).thenReturn(event);
        when(messageBuilders.get(0).buildMessage(event, Locale.getDefault())).thenReturn("Test message");
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        eventListener.onMessage(message, null);

        verify(notificationService).send(any(UserDto.class), eq("Test message"));
    }

    @Test
    public void testGetMessage_NoNotificationServiceFound() throws Exception {
        RecommendationReceivedEvent event = prepareEvent();
        UserDto user = prepareUser();

        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(message.getBody()).thenReturn(messageBody);

        when(userServiceClient.getUser(3L)).thenReturn(user);
        when(objectMapper.readValue(messageBody, RecommendationReceivedEvent.class)).thenReturn(event);
        when(messageBuilders.get(0).buildMessage(event, Locale.getDefault())).thenReturn("Test message");

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                eventListener.onMessage(message, null)
        );

        assertEquals("Mo notification service found for the user's preferred communication method.", exception.getMessage());
    }

    @Test
    public void testGetMessage_NoMessageBuilderFound() throws Exception {
        RecommendationReceivedEvent event = prepareEvent();

        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(message.getBody()).thenReturn(messageBody);

        when(objectMapper.readValue(messageBody, RecommendationReceivedEvent.class)).thenReturn(event);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                eventListener.onMessage(message, null)
        );

        assertEquals("Mo message builder found for the given event type: " + event.getClass().getName(), exception.getMessage());
    }

    private RecommendationReceivedEvent prepareEvent() {
        return new RecommendationReceivedEvent(
                1L,
                2L,
                3L,
                "Some message",
                LocalDateTime.of(2024, 12, 5, 15, 41, 16)
        );
    }

    private UserDto prepareUser() {
        return UserDto.builder()
                .id(3L)
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
    }
}
