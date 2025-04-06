package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AbstractEvenListenerTest {
    static class TestEvent {
        public String value;
    }

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MessageBuilder<TestEvent> messageBuilder;

    @Mock
    private NotificationService notificationService;

    @Mock
    private Message message;

    @Mock
    private Consumer<TestEvent> consumer;

    @Mock
    private ObjectMapper objectMapper;

    private TestEvent testEvent = new TestEvent();

    private AbstractEvenListener<TestEvent> abstractEvenListener;

    @BeforeEach
    public void setup() {
        testEvent.value = "test";
        abstractEvenListener = new AbstractEvenListener<TestEvent>(
                objectMapper,
                userServiceClient,
                List.of(messageBuilder),
                List.of(notificationService)
        ) {};
    }

    @Test
    public void testProcessEvent_success() throws Exception {
        String json = "{\"value\": \"test\"}";
        when(message.getBody()).thenReturn(json.getBytes(StandardCharsets.UTF_8));
        when(objectMapper.readValue(eq(json.getBytes(StandardCharsets.UTF_8)), (Class<Object>) any())).thenReturn(testEvent);

        abstractEvenListener.processEvent(message, TestEvent.class, consumer);

        verify(consumer).accept(any(TestEvent.class));
    }

    @Test
    void testGetMessage_success() {

    }
}
