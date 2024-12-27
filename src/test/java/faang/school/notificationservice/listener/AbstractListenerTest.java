package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.data.NotificationChannel;
import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.event.AchievementEvent;
import faang.school.notificationservice.event.EventHandler;
import faang.school.notificationservice.exception.EventDeserializationException;
import faang.school.notificationservice.exception.InvalidMessageException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.UserFeignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AbstractListenerTest {
    private static final String MESSAGE_BODY = "{\"message\":\"Test message\"}";

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private List<EventHandler<AchievementEvent>> eventHandlers;

    @Mock
    private List<NotificationService> notificationServices;

    @Mock
    private MessageBuilder<AchievementEvent> messageBuilder;

    @Mock
    private UserFeignService userFeignService;

    @Mock
    private Message message;

    @InjectMocks
    private AchievementEventListener listener;

    private AchievementEvent achievementEvent;



    @BeforeEach
    public void setUp() {
        achievementEvent = new AchievementEvent("User name", 1L, "Achievement title", 1L);
        listener = new AchievementEventListener(objectMapper, eventHandlers, notificationServices, messageBuilder, null);
    }

    @Test
    public void testGetEventType() {
        assertEquals(AchievementEvent.class, listener.getEventType());
    }

    @Test
    public void testListenEvent() throws IOException {
        when(message.getBody()).thenReturn(MESSAGE_BODY.getBytes(StandardCharsets.UTF_8));

        when(objectMapper.readValue(any(byte[].class), eq(AchievementEvent.class))).thenReturn(achievementEvent);

        AchievementEvent event = listener.listenEvent(message);

        assertEquals(achievementEvent, event);
    }

    @Test
    public void testListenEvent_EmptyMessage() {
        when(message.getBody()).thenReturn(new byte[0]);

        assertThrows(InvalidMessageException.class, () -> listener.listenEvent(message));
    }

    @Test
    public void testListenEvent_DeserializationError() throws IOException {
        when(message.getBody()).thenReturn(MESSAGE_BODY.getBytes(StandardCharsets.UTF_8));

        when(objectMapper.readValue(any(byte[].class), eq(AchievementEvent.class))).thenThrow(new IOException("Test error"));

        assertThrows(EventDeserializationException.class, () -> listener.listenEvent(message));
    }

    @Test
    public void testSendNotification_NotificationServiceFound() {
        UserContactsDto receiverDto = UserContactsDto.builder()
                .id(1L)
                .preference(NotificationChannel.EMAIL)
                .build();

        NotificationService notificationService = mock(NotificationService.class);
        when(notificationService.getPreferredContact()).thenReturn(NotificationChannel.EMAIL);
        when(notificationServices.stream()).thenReturn(Stream.of(notificationService));

        listener.sendNotification(receiverDto, achievementEvent, "Test message");

        verify(notificationService, times(1)).send(receiverDto, "Test message");
    }

    @Test
    public void testOnMessage() throws IOException {
        byte[] messageBodyBytes = MESSAGE_BODY.getBytes(StandardCharsets.UTF_8);
        when(message.getBody()).thenReturn(messageBodyBytes);
        when(objectMapper.readValue(messageBodyBytes, AchievementEvent.class)).thenReturn(achievementEvent);

        UserContactsDto userContactsDto = new UserContactsDto();
        userContactsDto.setId(1L);
        userContactsDto.setPreference(NotificationChannel.EMAIL);
        when(userFeignService.getUserContacts(1L)).thenReturn(userContactsDto);

        AchievementEventListener listener
                = new AchievementEventListener(objectMapper,eventHandlers, notificationServices, messageBuilder, userFeignService);

        listener.onMessage(message, "test-pattern".getBytes(StandardCharsets.UTF_8));

        verify(eventHandlers, times(1)).forEach(any());
        verify(userFeignService, times(1)).getUserContacts(1L);
    }
}