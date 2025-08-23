package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.RecommendationRequestedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationRequestedEventListenerTest {

    private ObjectMapper objectMapper;
    private NotificationService notificationService;
    private MessageBuilder<RecommendationRequestedEvent> messageBuilder;
    private RecommendationRequestedEventListener listener;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        notificationService = mock(NotificationService.class);
        messageBuilder = mock(MessageBuilder.class);
        listener = new RecommendationRequestedEventListener(objectMapper, notificationService, messageBuilder);
    }

    @Test
    void testOnMessageProcessesEvent() throws Exception {

        RecommendationRequestedEvent event = new RecommendationRequestedEvent(1L, 2L, 3L);
        String json = objectMapper.writeValueAsString(event);

        Message message = new DefaultMessage(
                "channel".getBytes(StandardCharsets.UTF_8),
                json.getBytes(StandardCharsets.UTF_8));

        when(messageBuilder.buildMessage(any(), any())).thenReturn("Test message");

        listener.onMessage(message, null);

        ArgumentCaptor<UserDto> userCaptor = ArgumentCaptor.forClass(UserDto.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        verify(notificationService).send(userCaptor.capture(), messageCaptor.capture());

        UserDto capturedUser = userCaptor.getValue();
        String capturedText = messageCaptor.getValue();

        assertEquals(2L, capturedUser.getId());
        assertEquals("Test message", capturedText);
    }


    @Test
    void testOnMessageWithInvalidJsonDoesNotThrowAndLogsError() {
        String invalidJson = "not-a-valid-json";
        Message message = new DefaultMessage(invalidJson.getBytes(StandardCharsets.UTF_8), "channel".getBytes());

        listener.onMessage(message, null);

        verify(notificationService, never()).send(any(), any());
    }

    @Test
    void testOnMessageWithNullMessageContent() throws Exception {
        RecommendationRequestedEvent event = new RecommendationRequestedEvent(1L, 2L, 3L);
        String json = objectMapper.writeValueAsString(event);

        when(messageBuilder.buildMessage(any(RecommendationRequestedEvent.class), any())).thenReturn(null);

        Message message = new DefaultMessage(
                "channel".getBytes(StandardCharsets.UTF_8),
                json.getBytes(StandardCharsets.UTF_8)
        );

        listener.onMessage(message, null);

        ArgumentCaptor<UserDto> userCaptor = ArgumentCaptor.forClass(UserDto.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        verify(notificationService).send(userCaptor.capture(), messageCaptor.capture());

        assertEquals(2L, userCaptor.getValue().getId());

        assertNull(messageCaptor.getValue());
    }

    @Test
    void testOnMessageWithEmptyMessageContent() throws Exception {
        RecommendationRequestedEvent event = new RecommendationRequestedEvent(1L, 2L, 3L);
        String json = objectMapper.writeValueAsString(event);

        Message message = new DefaultMessage(
                "channel".getBytes(StandardCharsets.UTF_8),
                json.getBytes(StandardCharsets.UTF_8)
        );

        when(messageBuilder.buildMessage(any(RecommendationRequestedEvent.class), any())).thenReturn("");

        listener.onMessage(message, null);

        verify(notificationService).send(any(UserDto.class), eq(""));
    }
}