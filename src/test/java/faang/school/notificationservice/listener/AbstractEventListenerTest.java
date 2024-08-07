package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.listener.EventHandlingException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
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
    private MessageBuilder<Object> messageBuilder;

    @Mock
    private NotificationService notificationService;

    private TestEventListener eventListener;
    private long userId;
    private String messageContent;
    private UserDto user;
    private Object event;
    private Locale locale;
    private Message redisMessage;
    private Consumer<Object> consumer;

    @BeforeEach
    void setUp() {
        userId = 1L;
        messageContent = "Message";
        user = mock(UserDto.class);
        event = new Object();
        locale = Locale.ENGLISH;
        redisMessage = mock(Message.class);
        consumer = mock(Consumer.class);
        eventListener = new TestEventListener(objectMapper, userServiceClient,
                List.of(messageBuilder), List.of(notificationService));
    }

    @Test
    void testHandleEventSuccess() throws IOException {
        when(redisMessage.getBody()).thenReturn(new byte[]{});
        when(objectMapper.readValue(any(byte[].class), eq(Object.class))).thenReturn(event);

        eventListener.handleEvent(redisMessage, Object.class, consumer);

        verify(consumer).accept(event);
    }

    @Test
    void testHandleEventInvalidJson() throws IOException {
        when(redisMessage.getBody()).thenReturn(new byte[]{});
        when(objectMapper.readValue(any(byte[].class), eq(Object.class))).thenThrow(IOException.class);

        assertThrows(EventHandlingException.class, () -> eventListener.handleEvent(redisMessage, Object.class, consumer));
    }

    @Test
    void testGetMessageSuccess() {
        when(messageBuilder.getInstance()).thenAnswer(invocation -> Object.class);
        when(messageBuilder.buildMessage(event, locale)).thenReturn(messageContent);

        String message = eventListener.getMessage(event, locale);
        assertEquals(messageContent, message);
    }

    @Test
    void testGetMessageNoMessageBuilderFound() {
        when(messageBuilder.getInstance()).thenAnswer(invocation -> String.class);

        assertThrows(NoSuchElementException.class, () -> eventListener.getMessage(event, locale));
    }

    @Test
    void testSendNotificationSuccess() {
        when(userServiceClient.getUser(userId)).thenReturn(user);
        when(user.getPreference()).thenReturn(UserDto.PreferredContact.EMAIL);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        eventListener.sendNotification(userId, messageContent);

        verify(notificationService).send(user, messageContent);
    }

    @Test
    void testSendNotificationNoNotificationServiceFound() {
        when(userServiceClient.getUser(userId)).thenReturn(user);
        when(user.getPreference()).thenReturn(UserDto.PreferredContact.EMAIL);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.SMS);

        assertThrows(NoSuchElementException.class, () -> eventListener.sendNotification(userId, messageContent));
    }

    @Test
    void testSendNotificationUserFetchFailure() {
        when(userServiceClient.getUser(userId)).thenThrow(HttpClientErrorException.class);

        assertThrows(RuntimeException.class, () -> eventListener.sendNotification(userId, messageContent));
    }

}