package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AbstractEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MessageBuilder<Object> messageBuilder;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private AbstractEventListener<Object> eventListener;

    @Test
    void testGetMessageSuccess() {
        Object event = new Object();
        Locale locale = Locale.ENGLISH;
        String expectedMessage = "Test message";

        when(messageBuilder.supportsEventType(event)).thenReturn(true);
        when(messageBuilder.buildMessage(event, locale)).thenReturn(expectedMessage);

        String result = eventListener.getMessage(event, locale);

        assertEquals(expectedMessage, result);
        verify(messageBuilder).supportsEventType(event);
        verify(messageBuilder).buildMessage(event, locale);
    }

    @Test
    void testGetMessageNoBuilderFound() {
        Object event = new Object();
        Locale locale = Locale.ENGLISH;

        when(messageBuilder.supportsEventType(event)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> eventListener.getMessage(event, locale));
    }

    @Test
    void testSendMessageSuccess() {
        long userId = 1L;
        String message = "Test message";
        UserDto user = new UserDto();
        user.setPreference(UserDto.PreferredContact.EMAIL);

        when(userServiceClient.getUser(userId)).thenReturn(user);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        eventListener.sendMessage(userId, message);

        verify(userServiceClient).getUser(userId);
        verify(notificationService).getPreferredContact();
        verify(notificationService).send(user, message);
    }

    @Test
    void testSendMessageNoServiceFound() {
        long userId = 1L;
        String message = "Test message";
        UserDto user = new UserDto();
        user.setPreference(UserDto.PreferredContact.PHONE);

        when(userServiceClient.getUser(userId)).thenReturn(user);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        assertThrows(IllegalArgumentException.class, () -> eventListener.sendMessage(userId, message));
    }
}



