package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ProjectFollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    private AbstractEventListener<Object> eventListener;


    @BeforeEach
    void setUp() {
        eventListener = new AbstractEventListener<Object>(
                objectMapper,
                userServiceClient,
                List.of(notificationService),
                List.of(messageBuilder),
                Object.class,
                messageBuilder
        ) {
            @Override
            protected List<UserDto> getNotifiedUsers(Object event) {
                return List.of();
            }

            @Override
            protected Object[] getArgs(Object event) {
                return new Object[0];
            }
        };
    }

    @Test
    void testGetMessageSuccess() {
        Object event = new Object();
        Locale locale = Locale.ENGLISH;
        String expectedMessage = "Test message";

        when(messageBuilder.getInstance()).thenReturn((Class) event.getClass());
        when(messageBuilder.buildMessage(event, locale, null)).thenReturn(expectedMessage);

        String message = eventListener.getMessage(event, locale, null);

        assertEquals(expectedMessage, message);
        verify(messageBuilder).buildMessage(event, locale, null);
    }

    @Test
    void testGetMessageThrowsExceptionWhenBuilderNotFound() {
        Object event = new Object();
        Locale locale = Locale.ENGLISH;

        when(messageBuilder.getInstance()).thenReturn((Class) String.class);

        assertThrows(IllegalArgumentException.class, () ->
                eventListener.getMessage(event, locale, null)
        );
    }

    @Test
    void testSendNotificationSuccess() {
        long userId = 1L;
        String message = "Notification message";
        UserDto userDto = new UserDto();
        userDto.setPreference(UserDto.PreferredContact.EMAIL);

        when(userServiceClient.getUser(userId)).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        eventListener.sendNotification(userId, message);

        verify(userServiceClient).getUser(userId);
        verify(notificationService).send(userDto, message);
    }

    @Test
    void testSendNotificationThrowsExceptionWhenServiceNotFound() {
        long userId = 1L;
        String message = "Notification message";
        UserDto userDto = new UserDto();
        userDto.setPreference(UserDto.PreferredContact.EMAIL);

        when(userServiceClient.getUser(userId)).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.SMS);

        assertThrows(IllegalArgumentException.class, () ->
                eventListener.sendNotification(userId, message)
        );
    }
}
