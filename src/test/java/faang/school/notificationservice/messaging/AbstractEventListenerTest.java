package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.model.EventType;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AbstractEventListenerTest {

    @Mock
    private MessageBuilder messageBuilder;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private NotificationService notificationService;

    private long id;

    private String message;
    private UserDto userDto;

    private Locale usLocale;
    private Class<?> postLike;

    private String[] messageArgs;

    private AbstractEventListener eventListener;
    private AbstractEventListener badEventListener;

    private List<MessageBuilder> messageBuilders;
    private List<NotificationService> notificationServices;

    @BeforeEach
    void setUp() {
        id = 1L;
        message = "Test message";

        userDto = UserDto.builder()
                .id(id)
                .email("mihusle@yandex.ru")
                .preference(UserDto.PreferredContact.EMAIL)
                .build();

        usLocale = Locale.US;
        postLike = EventType.class;

        messageArgs = new String[]{"Test", "message"};

        messageBuilders = List.of(messageBuilder);
        notificationServices = List.of(notificationService);

        badEventListener = new AbstractEventListener(null, userServiceClient, List.of(), List.of()) {};
        eventListener = new AbstractEventListener(null, userServiceClient, messageBuilders, notificationServices) {};

    }

    @Test
    @DisplayName("Get message: Positive scenario")
    void testGetMessage_IsOk() {
        when(messageBuilder.supportsEventType(postLike)).thenReturn(true);
        when(messageBuilder.buildMessage(postLike, usLocale, messageArgs)).thenReturn(message);

        String message = eventListener.getMessage(postLike, usLocale, messageArgs);

        Assertions.assertEquals("Test message", message);
    }

    @Test
    @DisplayName("Get message: No message builder found")
    void testGetMessage_NoMessageBuilder() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> badEventListener.getMessage(postLike, usLocale, messageArgs));
    }

    @Test
    @DisplayName("Send notification: Positive scenario")
    void testSendNotification_IsOk() {
        when(userServiceClient.getUser(userDto.getId())).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);
        eventListener.sendNotification(userDto.getId(), message);
        verify(notificationService).send(userDto, message);
    }

    @Test
    @DisplayName("Send notification: No notification service found")
    void testSendNotification_NoNotificationService() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> badEventListener.sendNotification(id, message));
    }
}
