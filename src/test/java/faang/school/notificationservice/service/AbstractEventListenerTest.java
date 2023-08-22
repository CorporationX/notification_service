package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.DataValidationException;
import faang.school.notificationservice.massages.MessageBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractEventListenerTest {

    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private NotificationService notificationService;
    @Mock
    private MessageBuilder messageBuilder;
    private UserDto userDto;

    private Locale usLocale;
    private Class<?> postLike;

    private AbstractEventListener eventListener;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(1L)
                .email("M@A")
                .preference(UserDto.PreferredContact.EMAIL)
                .build();

        usLocale = Locale.US;
        postLike = Optional.empty().getClass();

        eventListener = new AbstractEventListener(null, userServiceClient, List.of(notificationService), List.of(messageBuilder)) {
        };
    }

    @Test
    void testGetMessageDataValidationException() {
        when(messageBuilder.supportsEventType(postLike)).thenReturn(true);

        assertThrows(DataValidationException.class,
                () -> eventListener.getMessage(postLike, usLocale));
    }

    @Test
    void testGetMessage() {
        when(messageBuilder.supportsEventType(postLike)).thenReturn(true);
        when(messageBuilder.buildMessage(postLike, usLocale)).thenReturn("Test");

        String message = eventListener.getMessage(postLike, usLocale);
        assertEquals("Test", message);
    }

    @Test
    void testSendNotificationDataValidationException() {
        when(userServiceClient.getUser(userDto.getId())).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.SMS);

        assertThrows(DataValidationException.class,
                () -> eventListener.sendNotification(1L, "Test"));
    }

    @Test
    void testSendNotification() {
        when(userServiceClient.getUser(userDto.getId())).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        eventListener.sendNotification(userDto.getId(), "Test");
        verify(notificationService).send(userDto, "Test");
    }
}