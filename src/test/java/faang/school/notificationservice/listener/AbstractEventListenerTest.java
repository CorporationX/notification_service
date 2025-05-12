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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AbstractEventListenerTest {

    static class TestEvent {
        String content = "Hello!";
        Locale locale = Locale.getDefault();
    }

    static class TestEventListener extends AbstractEventListener<TestEvent> {

        public TestEventListener(ObjectMapper objectMapper,
                                 UserServiceClient userServiceClient,
                                 Map<Class<?>, MessageBuilder<?>> messageBuilderMap,
                                 List<NotificationService> notificationServices) {
            super(objectMapper, userServiceClient, notificationServices, messageBuilderMap);
        }

        @Override
        public void onMessage(Message message, byte[] pattern) {
        }
    }

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private MessageBuilder<TestEvent> messageBuilder;
    @Mock
    private NotificationService emailNotificationService;

    private TestEventListener testEventListener;

    private TestEvent testEvent;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        testEvent = new TestEvent();

        userDto = new UserDto();
        userDto.setPreference(UserDto.PreferredContact.EMAIL);

        Map<Class<?>, MessageBuilder<?>> messageBuilderMap = new HashMap<>();
        messageBuilderMap.put(TestEvent.class, messageBuilder);

        testEventListener = new TestEventListener(
                objectMapper,
                userServiceClient,
                messageBuilderMap,
                List.of(emailNotificationService)
        );
    }

    @Test
    public void testGetMessageSuccess() {
        when(messageBuilder.buildMessage(testEvent, testEvent.locale)).thenReturn(testEvent.content);

        String result = testEventListener.getMessage(testEvent, testEvent.locale);
        assertEquals(testEvent.content, result);
        verify(messageBuilder, times(1)).buildMessage(testEvent, testEvent.locale);
    }

    @Test
    public void testGetMessageFailure() {
        TestEventListener listenerWithoutBuilder = new TestEventListener(
                objectMapper,
                userServiceClient,
                new HashMap<>(),
                List.of(emailNotificationService)
        );

        String error = "No MessageBuilder found for: " + TestEvent.class.getName();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> listenerWithoutBuilder.getMessage(testEvent, testEvent.locale));

        assertEquals(error, exception.getMessage());
    }

    @Test
    public void testSendNotificationSuccess() {
        when(userServiceClient.getUser(1L)).thenReturn(userDto);
        when(emailNotificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        testEventListener.sendNotification(1L, anyString());

        verify(userServiceClient, times(1)).getUser(anyLong());
        verify(emailNotificationService, times(1)).getPreferredContact();
    }

    @Test
    public void testSendNotificationUserFailure() {
        String error = "No NotificationService for contact: " + userDto.getPreference();
        when(userServiceClient.getUser(1L)).thenReturn(userDto);
        when(emailNotificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.PHONE);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> testEventListener.sendNotification(1L, anyString()));
        assertEquals(error, exception.getMessage());
        verify(userServiceClient, times(1)).getUser(anyLong());
        verify(emailNotificationService, times(1)).getPreferredContact();

    }
}
