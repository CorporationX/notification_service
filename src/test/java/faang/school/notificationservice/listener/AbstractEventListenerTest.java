package faang.school.notificationservice.listener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.connection.Message;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.config.context.UserContext;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AbstractEventListenerTest {

    @Mock
    private List<MessageBuilder<String>> messageBuilders;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private List<NotificationService> notificationServices;

    @Mock
    private UserContext userContext;

    @Mock
    private MessageBuilder<String> messageBuilder;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private TestEventListener testEventListener;

    private Map<EventType, MessageBuilder<String>> messageBuildersMap;
    private Map<UserDto.PreferredContact, NotificationService> notificationServiceMap;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        messageBuildersMap = new HashMap<>();
        messageBuildersMap.put(EventType.EVENT_TYPE_RECOMMENDATION, messageBuilder);
        when(messageBuilder.getEventType()).thenReturn(EventType.EVENT_TYPE_RECOMMENDATION);

        notificationServiceMap = new HashMap<>();
        notificationServiceMap.put(UserDto.PreferredContact.TELEGRAM, notificationService);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.TELEGRAM);

        testEventListener = new TestEventListener(messageBuilders,
                objectMapper, userServiceClient, notificationServices, userContext);
    }

    @Test
    void testHandleEventSuccess() throws IOException {
        Message message = mock(Message.class);
        String event = "test event";
        when(message.getBody()).thenReturn("test event".getBytes());
        when(objectMapper.readValue(any(byte[].class), eq(String.class))).thenReturn(event);

        testEventListener.handleEvent(message, String.class, e -> {
            assertEquals(event, e);
        });

        verify(objectMapper, times(1)).readValue(any(byte[].class), eq(String.class));
    }

    @Test
    void testHandleEventThrowsException() throws IOException {
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn("test event".getBytes());
        when(objectMapper.readValue(any(byte[].class), eq(String.class)))
                .thenThrow(new IOException("Deserialization error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            testEventListener.handleEvent(message, String.class, e -> {});
        });

        verify(objectMapper, times(1)).readValue(any(byte[].class), eq(String.class));
        assertEquals(String.format("Error deserializing JSON to object"),
                exception.getMessage());
    }

    @Test
    void testGetMessageThrowsException() {
        String event = "test event";
        doNothing().when(messageBuildersMap.get(EventType.EVENT_TYPE_RECOMMENDATION));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> testEventListener.getMessage(1L, event));

        assertEquals(String.format("No message builder found for the given event type: EVENT_TYPE_RECOMMENDATION"),
                exception.getMessage());
    }

    @Test
    void testSendNotificationThrowsException() {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setPreference(UserDto.PreferredContact.EMAIL);

        when(userServiceClient.getUser(1L)).thenReturn(user);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            testEventListener.sendNotification(1L, "Test message");
        });

        assertEquals(String.format("No notification service found for the user's preferred communication method: EMAIL"),
                exception.getMessage());
    }
}

class TestEventListener extends AbstractEventListener<String> {

    public TestEventListener(List<MessageBuilder<String>> messageBuilders,
                             ObjectMapper objectMapper,
                             UserServiceClient userServiceClient,
                             List<NotificationService> notificationServices,
                             UserContext userContext) {
        super(messageBuilders, objectMapper, userServiceClient, notificationServices, userContext);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
    }

    @Override
    public EventType getEventType() {
        return EventType.EVENT_TYPE_RECOMMENDATION;
    }
}
