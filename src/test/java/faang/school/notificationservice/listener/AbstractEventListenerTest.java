package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.EventHandlingException;
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
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
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
    private MessageBuilder<Object> messageBuilder;

    private TestEventListener testEventListener;

    @BeforeEach
    void setUp() {
        testEventListener = new TestEventListener(objectMapper,
                userServiceClient,
                List.of(notificationService),
                List.of(messageBuilder));
    }

    @Test
    void testHandleEvent_Successful() throws IOException {
        Message message = mock(Message.class);
        Consumer<Object> consumer = mock(Consumer.class);
        Object event = new Object();
        when(objectMapper.readValue(message.getBody(), Object.class)).thenReturn(event);

        testEventListener.handleEvent(message, Object.class, consumer);

        verify(consumer).accept(event);
    }

    @Test
    void testHandleEvent_ThrowsException() throws IOException {
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(new byte[0]);
        when(objectMapper.readValue(message.getBody(), Object.class)).thenThrow(new IOException());

        EventHandlingException thrownException = assertThrows(EventHandlingException.class,
                () -> testEventListener.handleEvent(message, Object.class, mock(Consumer.class)));

        assertEquals("Error while handling " + Object.class.getName() + " from Redis message",
                thrownException.getMessage());
    }

    @Test
    void testGetMessage_Successful() {
        Object event = new Object();
        Locale locale = Locale.getDefault();
        when(messageBuilder.getInstance()).thenReturn(Object.class);
        when(messageBuilder.buildMessage(event, locale)).thenReturn("test message");

        testEventListener.getMessage(event, locale);

        verify(messageBuilder).buildMessage(event, locale);
    }

    @Test
    void testGetMessage_ThrowsException() {
        String event = "event different from Object";
        Locale locale = Locale.getDefault();
        when(messageBuilder.getInstance()).thenReturn(Object.class);

        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class,
                () -> testEventListener.getMessage(event, locale));
        assertEquals("No message builder found for event: " + event.getClass().getName(),
                thrownException.getMessage());
    }

    @Test
    void testSendNotification_Successful() {
        long userId = 1L;
        String message = "test message";
        UserDto userDto = UserDto.builder()
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
        when(userServiceClient.getUser(userId)).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        testEventListener.sendNotification(userId, message);

        verify(notificationService).send(userDto, message);
    }

    @Test
    void testSendNotification_ThrowsException() {
        long userId = 1L;
        String message = "test message";
        UserDto userDto = UserDto.builder()
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
        when(userServiceClient.getUser(userId)).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.TELEGRAM);

        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class,
                () -> testEventListener.sendNotification(userId, message));

        assertEquals("No notification service found for the user preferred communication method: "
                + userDto.getPreference(), thrownException.getMessage());
    }

    private static class TestEventListener extends AbstractEventListener<Object> {
        public TestEventListener(ObjectMapper objectMapper,
                                 UserServiceClient userServiceClient,
                                 List<NotificationService> notificationServices,
                                 List<MessageBuilder<Object>> messageBuilders) {
            super(objectMapper, userServiceClient, notificationServices, messageBuilders);
        }
    }
}