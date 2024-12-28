package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.data.NotificationChannel;
import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.event.EventStartEvent;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventStartEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private NotificationService notificationService;

    @Mock
    private MessageBuilder<EventStartEvent> messageBuilder;

    @InjectMocks
    private EventStartEventListener eventStartEventListener;

    @BeforeEach
    void setUp() {
        eventStartEventListener = new EventStartEventListener(
                objectMapper,
                userServiceClient,
                List.of(messageBuilder),
                List.of(notificationService)
        );
    }

    @Test
    @DisplayName("Deserialization success")
    void testDeserialization_Success() throws Exception {
        String json = "{\"eventId\":5,\"eventTitle\":\"testTitle\"}";
        ObjectMapper objectMapper = new ObjectMapper();

        EventStartEvent event = objectMapper.readValue(json, EventStartEvent.class);

        assertNotNull(event);
        assertEquals(5L, event.eventId());
    }

    @Test
    @DisplayName("On message should process message and send notification")
    void onMessageShouldProcessMessageAndSendNotification() throws Exception {
        EventStartEvent eventStartEvent = EventStartEvent.builder()
                .eventId(1L)
                .eventTitle("Test Event")
                .eventStartTime(LocalDateTime.now())
                .attendeesIds(List.of(1L, 2L))
                .build();

        UserContactsDto firstUserContactsDto = UserContactsDto.builder()
                .id(1L)
                .email("test@example.com")
                .username("test")
                .preference(NotificationChannel.EMAIL)
                .build();

        UserContactsDto secondUserContactsDto = UserContactsDto.builder()
                .id(2L)
                .email("test@example.com")
                .username("test")
                .preference(NotificationChannel.EMAIL)
                .build();

        Message redisMessage = mock(Message.class);
        byte[] rawMessage = "{}".getBytes();
        when(redisMessage.getBody()).thenReturn(rawMessage);
        when(objectMapper.readValue(any(byte[].class), eq(EventStartEvent.class))).thenReturn(eventStartEvent);
        when(userServiceClient.getUserContacts(1L)).thenReturn(firstUserContactsDto);
        when(userServiceClient.getUserContacts(2L)).thenReturn(secondUserContactsDto);
        when(messageBuilder.buildMessage(eventStartEvent, Locale.getDefault())).thenReturn("test message");
        when(messageBuilder.getInstance()).thenReturn(EventStartEvent.class);
        when(notificationService.getPreferredContact()).thenReturn(NotificationChannel.EMAIL);

        eventStartEventListener.onMessage(redisMessage, null);

        verify(objectMapper, times(1)).readValue(any(byte[].class), eq(EventStartEvent.class));
        verify(messageBuilder, times(2)).buildMessage(eventStartEvent, Locale.getDefault());
        verify(userServiceClient, times(2)).getUserContacts(1L);
        verify(userServiceClient, times(2)).getUserContacts(2L);
        verify(notificationService, times(1)).send(firstUserContactsDto, "test message");
        verify(notificationService, times(1)).send(secondUserContactsDto, "test message");
    }
}
