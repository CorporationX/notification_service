package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AbstractEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private NotificationService notificationService;

    @Mock
    private MessageBuilder<TestEvent> messageBuilder;


    private TestEventListener eventListener;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        eventListener = new TestEventListener(objectMapper, userServiceClient, List.of(notificationService), List.of(messageBuilder));
    }

    @Test
    public void testGetMessage() {
        // Arrange
        Locale locale = Locale.ENGLISH;
        TestEvent event = new TestEvent("Test message");
        when(messageBuilder.getInstance()).thenReturn(TestEvent.class);
        when(messageBuilder.buildMessage(event, locale)).thenReturn("Formatted message");

        // Act
        String message = eventListener.getMessage(event, locale);

        // Assert
        assertEquals("Formatted message", message);
        verify(messageBuilder).buildMessage(event, locale);
    }

    @Test
    public void testGetMessageThrowsExceptionWhenNoBuilderFound() {
        // Arrange
        TestEvent event = new TestEvent("Test message");
        Locale locale = Locale.ENGLISH;
        when(messageBuilder.getInstance()).thenThrow(new IllegalArgumentException("No message builder found for " + TestEvent.class.getName()));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                eventListener.getMessage(event, locale)
        );
        assertEquals("No message builder found for " + TestEvent.class.getName(), exception.getMessage());
    }

    @Test
    public void testSentNotification() {
        // Arrange
        long userId = 1L;
        String message = "Test notification";
        UserDto user = new UserDto();
        user.setId(userId);
        user.setPreference(UserDto.PreferredContact.EMAIL);
        user.setEmail("test@example.com");

        when(userServiceClient.getUser(userId)).thenReturn(user);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        // Act
        eventListener.sentNotification(userId, message);

        // Assert
        verify(userServiceClient).getUser(userId);
        verify(notificationService).send(user, message);
    }

    @Test
    public void testSentNotificationThrowsExceptionWhenNoServiceFound() {
        // Arrange
        long userId = 1L;
        String message = "Test notification";
        UserDto user = new UserDto();
        user.setId(userId);
        user.setPreference(UserDto.PreferredContact.EMAIL);
        user.setEmail("test@example.com");

        when(userServiceClient.getUser(userId)).thenReturn(user);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.SMS); // Не совпадает с предпочтением

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            eventListener.sentNotification(userId, message)
        );
        assertEquals("Notification service not found, not preferred method for sending message", exception.getMessage());
    }

    // Вспомогательный класс для тестирования
    @Component
    private static class TestEventListener extends AbstractEventListener<TestEvent> {
        public TestEventListener(com.fasterxml.jackson.databind.ObjectMapper objectMapper,
                                 UserServiceClient userServiceClient,
                                 List<NotificationService> notificationServices,
                                 List<MessageBuilder<TestEvent>> messageBuilders) {
            super(objectMapper, userServiceClient, notificationServices, messageBuilders);
        }

        @Override
        public void onMessage(Message message, byte[] pattern) {

        }
    }

    // Вспомогательный класс для тестирования
    private static class TestEvent {
        private final String message;

        public TestEvent(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
