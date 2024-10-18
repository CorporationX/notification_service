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
    private MessageBuilder<TestEvent> messageBuilder;

    @InjectMocks
    private TestEventListener eventListener;

    private final UserDto emptyUser = new UserDto();
    private final TestEvent event = new TestEvent();
    private final String message = "MESSAGE";
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
        when(messageBuilder.getInstance()).thenReturn(TestEvent.class);
        when(messageBuilder.buildMessage(emptyUser, event)).thenReturn(message);

        String actualMessage = eventListener.getMessage(emptyUser, event);

        assertEquals(message, actualMessage);
        verify(messageBuilder).buildMessage(emptyUser, event);
    }

    @Test
    @SuppressWarnings("unchecked")
    void getMessage_NoHandler_ThrowsException() {
        String correctMessage = "No such event handler for TestEvent";
        when(messageBuilder.getInstance()).thenReturn((Class) SomeOtherEvent.class);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> eventListener.getMessage(emptyUser, event));

        assertEquals(correctMessage, exception.getMessage());
    }

    @Test
    void sendNotification_UserFound_Success() {
        UserDto userDto = UserDto.builder()
                .id(userId)
                .email("test@example.com")
                .preferredContact(UserDto.PreferredContact.EMAIL)
                .build();
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        eventListener.sendNotification(userDto, message);
        verify(notificationService).send(userDto, message);
    }

    @Test
    void sendNotification_NoPreferredService_ThrowsException() {
        String correctMessage = "Notification service not found for user " + userId;
        UserDto userDto = UserDto.builder()
                .id(userId)
                .preferredContact(UserDto.PreferredContact.PHONE)
                .build();
        ReflectionTestUtils.setField(eventListener, "notificationServices", Collections.emptyList());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                eventListener.sendNotification(userDto, message));

        assertEquals(correctMessage, exception.getMessage());
    }

    private static class TestEventListener extends AbstractEventListener<TestEvent> {
        public TestEventListener(ObjectMapper objectMapper,
                                 UserServiceClient userServiceClient,
                                 List<NotificationService> notificationServices,
                                 List<MessageBuilder<TestEvent>> messageBuilders) {
            super(objectMapper, userServiceClient, notificationServices, messageBuilders);
        }

        @Override
        public void onMessage(Message message, byte[] pattern) {

        }
    }

    private static class TestEvent {
    }

    private static class SomeOtherEvent {
    }
}
