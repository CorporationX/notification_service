package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
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
    private NotificationService<TestMessage> notificationService;

    @Mock
    private MessageBuilder<TestEvent, TestMessage> messageBuilder;

    @InjectMocks
    private TestEventListener eventListener;

    private final Locale locale = Locale.ENGLISH;
    private final TestEvent event = new TestEvent();
    private final TestMessage message = new TestMessage();
    private final long userId = 1L;

    @BeforeEach
    void setUp() {
        eventListener = new TestEventListener(
                objectMapper,
                userServiceClient,
                List.of(notificationService),
                List.of(messageBuilder)
        );
    }

    @Test
    void getMessage_ValidEvent_ReturnsMessage() {
        TestMessage expectedMessage = new TestMessage();
        when(messageBuilder.getInstance()).thenReturn(TestEvent.class);
        when(messageBuilder.buildMessage(event, locale)).thenReturn(expectedMessage);

        TestMessage actualMessage = eventListener.getMessage(event, locale);

        assertEquals(expectedMessage, actualMessage);
        verify(messageBuilder).buildMessage(event, locale);
    }

    @Test
    @SuppressWarnings("unchecked")
    void getMessage_NoHandler_ThrowsException() {
        String correctMessage = "No such event handler for TestEvent";
        when(messageBuilder.getInstance()).thenReturn((Class) SomeOtherEvent.class);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> eventListener.getMessage(event, locale));

        assertEquals(correctMessage, exception.getMessage());
    }

    @Test
    void sendNotification_UserFound_Success() {
        UserDto userDto = UserDto.builder()
                .id(userId)
                .email("test@example.com")
                .preference(UserDto.PreferredContact.EMAIL)
                .build();

        when(userServiceClient.tryGetUser(userId)).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        eventListener.sendNotification(userId, message);
        verify(notificationService).send(userDto, message);
    }

    @Test
    void sendNotification_UserNotFound_ThrowsException() {
        when(userServiceClient.tryGetUser(userId)).thenThrow(new RuntimeException("User not found"));

        Exception exception = assertThrows(RuntimeException.class, () ->
                eventListener.sendNotification(userId, message));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void sendNotification_NoPreferredService_ThrowsException() {
        String correctMessage = "Notification service not found for user " + userId;
        UserDto userDto = UserDto.builder().preference(UserDto.PreferredContact.SMS).build();
        ReflectionTestUtils.setField(eventListener, "notificationServices", Collections.emptyList());

        when(userServiceClient.tryGetUser(userId)).thenReturn(userDto);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                eventListener.sendNotification(userId, message));

        assertEquals(correctMessage, exception.getMessage());
    }

    private static class TestEventListener extends AbstractEventListener<TestEvent, TestMessage> {
        public TestEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                 List<NotificationService<TestMessage>> notificationServices,
                                 List<MessageBuilder<TestEvent, TestMessage>> messageBuilders) {
            super(objectMapper, userServiceClient, notificationServices, messageBuilders);
        }

        @Override
        public void onMessage(Message message, byte[] pattern) {

        }
    }

    private static class TestEvent {
    }

    private static class TestMessage {
    }

    private static class SomeOtherEvent {
    }
}
