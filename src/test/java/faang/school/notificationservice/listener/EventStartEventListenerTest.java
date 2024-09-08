package faang.school.notificationservice.listener;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.publishable.EventStartEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.connection.Message;

import java.util.Collections;

public class EventStartEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private NotificationService notificationService;

    @Mock
    private MessageBuilder<?> messageBuilder;

    @InjectMocks
    private EventStartEventListener eventStartEventListener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(messageBuilder.getInstance()).thenAnswer(invocation -> EventStartEvent.class);

        eventStartEventListener = new EventStartEventListener(
                objectMapper,
                userServiceClient,
                Collections.singletonList(notificationService),
                Collections.singletonList((MessageBuilder<EventStartEvent>) messageBuilder)
        );
    }

    @Test
    void testHandleEventException() throws Exception {
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn("".getBytes());
        when(objectMapper.readValue(message.getBody(), EventStartEvent.class))
                .thenThrow(new RuntimeException("Deserialization error"));

        assertThrows(RuntimeException.class, () -> {
            eventStartEventListener.onMessage(message, new byte[0]);
        });
    }
}
