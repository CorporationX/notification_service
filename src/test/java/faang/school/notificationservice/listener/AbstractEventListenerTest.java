package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private NotificationService notificationService;

    @Mock
    private MessageBuilder<Event> messageBuilder;

    @Mock
    private Message message;

    private AbstractEventListener<Event> abstractEventListener;

    private AbstractEventListenerTest.Event event;
    private byte[] messageBody;
    private Consumer<Event> consumer;
    private String messageBuild;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        messageBody = new byte[100];
        event = new Event("Event");
        messageBuild = "message";
        consumer = System.out::print;
        userDto = UserDto.builder()
                .id(1L)
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
        abstractEventListener = new AbstractEventListener<>(
                objectMapper,
                userServiceClient,
                List.of(notificationService),
                List.of(messageBuilder)) {
        };
    }

    @Test
    @DisplayName("testing handleEvent with positive execution")
    void handleEventWithSuccessfulParsing() throws IOException {
        when(message.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, Event.class)).thenReturn(event);

        abstractEventListener.handleEvent(message, Event.class, consumer);

        verify(objectMapper, times(1)).readValue(messageBody, Event.class);
    }

    @Test
    @DisplayName("testing handleEvent with exception throwing execution")
    void testHandleEventWithParsingException() throws IOException {
        when(message.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, Event.class)).thenThrow(new IOException());

        assertThrows(RuntimeException.class, () -> abstractEventListener.handleEvent(message, Event.class, consumer));
    }

    @Test
    @DisplayName("testing getMessage with matched message builder")
    void testGetMessageWithFoundMessageBuilder() {
        when(messageBuilder.getInstance()).thenReturn(Event.class);
        when(messageBuilder.buildMessage(event, Locale.ENGLISH)).thenReturn(messageBuild);
        String result = abstractEventListener.getMessage(event, Locale.ENGLISH);

        assertEquals(messageBuilder.buildMessage(event, Locale.ENGLISH), result);
    }

    @Test
    @DisplayName("testing getMessage with no matched message builder")
    void testGetMessageWithNoMatchedMessageBuilder() {
        assertThrows(IllegalArgumentException.class, () -> abstractEventListener.getMessage(event, Locale.ENGLISH));
    }

    @Test
    @DisplayName("testing sendNotification with matched notification service")
    void testSendNotificationWithFoundNotificationService() {
        userDto.setPreference(UserDto.PreferredContact.EMAIL);
        when(userServiceClient.getUser(1L)).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        abstractEventListener.sendNotification(1L, messageBuild);

        verify(notificationService, times(1)).send(userDto, messageBuild);
    }

    @Test
    @DisplayName("testing sendNotification with no matched notification service")
    void testSendNotificationWithNoNotificationServiceFound() {
        userDto.setPreference(UserDto.PreferredContact.EMAIL);
        when(userServiceClient.getUser(1L)).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.SMS);

        assertThrows(IllegalArgumentException.class,
                () -> abstractEventListener.sendNotification(1L, "Test message"));
    }

    @Data
    public static class Event {
        private String name;
        public Event(String name) {
            this.name = name;
        }
    }
}