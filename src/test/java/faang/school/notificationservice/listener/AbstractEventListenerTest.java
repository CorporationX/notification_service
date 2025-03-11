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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
    private long userId;
    private String messageContent;
    private UserDto user;
    private Object event;
    private Locale locale;
    private Message message;
    private Consumer<Object> consumer;

    @BeforeEach
    void setUp() {
        userId = 1L;
        messageContent = "Message";
        user = mock(UserDto.class);
        event = new Object();
        locale = Locale.ENGLISH;
        message = mock(Message.class);
        consumer = mock(Consumer.class);
        testEventListener = new TestEventListener(objectMapper,
                userServiceClient,
                List.of(notificationService),
                List.of(messageBuilder));
    }

    @Test
    void testHandleEvent() throws IOException {
        when(message.getBody()).thenReturn(new byte[]{});
        when(objectMapper.readValue(any(byte[].class), eq(Object.class))).thenReturn(event);

        testEventListener.handleEvent(message, Object.class, consumer);

        verify(consumer).accept(event);
    }

    @Test
    void testHandleEventInvalidJson() throws IOException {
        when(message.getBody()).thenReturn(new byte[]{});
        when(objectMapper.readValue(any(byte[].class), eq(Object.class))).thenThrow(IOException.class);

        assertThrows(EventHandlingException.class,
                () -> testEventListener.handleEvent(message, Object.class, consumer));
    }

    @Test
    void testGetMessageSuccess() {
        when(messageBuilder.getInstance()).thenAnswer(invocation -> Object.class);
        when(messageBuilder.buildMessage(event, locale)).thenReturn(messageContent);

        String result = testEventListener.getMessage(event, locale);

        assertEquals(messageContent, result);
        verify(messageBuilder).buildMessage(event, locale);
    }

    @Test
    void testGetMessageNoMessageBuilderFound() {
        when(messageBuilder.getInstance()).thenAnswer(invocation -> String.class);

        assertThrows(IllegalArgumentException.class, () -> testEventListener.getMessage(event, locale));
    }


    @Test
    void testSendNotificationSuccess() {
        when(userServiceClient.getUser(userId)).thenReturn(user);
        when(user.getPreference()).thenReturn(UserDto.PreferredContact.TELEGRAM);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.TELEGRAM);

        testEventListener.sendNotification(userId, messageContent);

        verify(notificationService).send(user, messageContent);
    }

    @Test
    void testSendNotificationNoNotificationServiceFound() {
        when(userServiceClient.getUser(userId)).thenReturn(user);
        when(user.getPreference()).thenReturn(null);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.TELEGRAM);

        assertThrows(IllegalArgumentException.class, () -> testEventListener.sendNotification(userId, messageContent));
    }

    static class TestEventListener extends AbstractEventListener<Object> {
        public TestEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                 List<NotificationService> notificationServices,
                                 List<MessageBuilder<Object>> messageBuilders) {
            super(objectMapper, userServiceClient, notificationServices, messageBuilders);
        }
    }
}