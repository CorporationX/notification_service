package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;


@ExtendWith(MockitoExtension.class)
class EventsListenerTest {

    private ObjectMapper objectMapper;
    private UserServiceClient userServiceClient;
    private MessageBuilder<Object> messageBuilder;
    private NotificationService notificationService;
    private TestEventsListener eventsListener;


    private Object testEvent;
    private Message message;
    private Consumer<Object> consumer;
    private Locale locale;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        objectMapper = mock(ObjectMapper.class);
        userServiceClient = mock(UserServiceClient.class);
        messageBuilder = mock(MessageBuilder.class);
        notificationService = mock(NotificationService.class);
        eventsListener = new TestEventsListener(objectMapper, userServiceClient,
                List.of(messageBuilder), List.of(notificationService));

        testEvent = new Object();
        message = mock(Message.class);
        locale = Locale.ENGLISH;
        userDto = mock(UserDto.class);
        consumer = mock((Class<Consumer<Object>>) (Class<?>) Consumer.class);

    }

    @Test
    void handleEventTest() throws IOException {

        when(message.getBody()).thenReturn(new byte[]{});
        when(objectMapper.readValue(any(byte[].class), eq(Object.class))).thenReturn(testEvent);

        eventsListener.handleEvent(message, Object.class, consumer);

        verify(consumer).accept(testEvent);
    }


    @Test
    void handleEventThrowException() throws IOException {

        when(message.getBody()).thenReturn(new byte[]{});
        when(objectMapper.readValue(any(byte[].class), eq(Object.class))).thenThrow(new IOException());

        assertThrows(RuntimeException.class,
                () -> eventsListener.handleEvent(message, Object.class, consumer));
    }

    @Test
    void getMessageTestSuccess() {

        when(messageBuilder.supportEventType()).thenReturn(Object.class);
        when(messageBuilder.buildMessage(testEvent, locale)).thenReturn("Test Message");

        String message = eventsListener.getMessage(testEvent, locale);

        verify(messageBuilder).buildMessage(testEvent, locale);
        assertEquals("Test Message", message);
    }

    @Test
    void getMessageTestThrowException() {

        when(messageBuilder.supportEventType()).thenAnswer(invocation -> String.class);

        assertThrows(IllegalArgumentException.class, () -> eventsListener.getMessage(testEvent, locale));
    }

    @Test
    void sendNotificationTestSuccess() {

        when(userDto.getPreference()).thenReturn(UserDto.PreferredContact.EMAIL);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        eventsListener.sendNotification(userDto, "Test notification");

        verify(notificationService).send(userDto, "Test notification");
    }

    @Test
    void sendNotificationTestFail() {

        when(userDto.getPreference()).thenReturn(UserDto.PreferredContact.SMS);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        assertThrows(IllegalArgumentException.class,
                () -> eventsListener.sendNotification(userDto, "Test notification"));

        verify(notificationService, never()).send(any(), any());
    }

    static class TestEventsListener extends EventsListener<Object> {
        public TestEventsListener(ObjectMapper objectMapper,
                                              UserServiceClient userClient,
                                              List<MessageBuilder<Object>> messageBuilders,
                                              List<NotificationService> notifyServices) {
            super(objectMapper, userClient, messageBuilders, notifyServices);
        }
    }
}
