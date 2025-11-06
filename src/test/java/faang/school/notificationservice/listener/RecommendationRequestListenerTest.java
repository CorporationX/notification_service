package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationRequestEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RecommendationRequestListenerTest {
    @Mock
    protected ObjectMapper objectMapper;
    @Mock
    protected UserServiceClient userServiceClient;
    @Mock
    private NotificationService notificationServices;
    @Mock
    protected MessageBuilder<RecommendationRequestEvent> messageBuilders;

    @InjectMocks
    @Spy
    RecommendationRequestListener listener;

    @Test
    void onMessageNotificationProcessSuccesses() {
        RecommendationRequestEvent event = new RecommendationRequestEvent(1L, 20L, 30L, "text");
        byte[] body = "{\"id\":1,\"requesterId\":20,\"recommenderId\":30,\"text\":\"text\"}".getBytes(StandardCharsets.UTF_8);
        byte[] channel = "recommendation-channel".getBytes(StandardCharsets.UTF_8);
        Message message = new DefaultMessage(channel, body);

        doAnswer(invocation -> {
            var consumer = invocation.getArgument(2, java.util.function.Consumer.class);
            consumer.accept(event);
            return null;
        }).when(listener).handleEvent(any(), eq(RecommendationRequestEvent.class), any());

        doReturn("test notification").when(listener).getMessage(event, Locale.UK);
        doNothing().when(listener).sendNotification(anyLong(), anyString());

        listener.onMessage(message, null);

        verify(listener).getMessage(event, Locale.UK);
        verify(listener).sendNotification(30L, "test notification");
    }

    @Test
    void onMessage_WhenNoMessageBuilder_ShouldThrowException() {
        RecommendationRequestEvent event = new RecommendationRequestEvent(1L, 20L, 30L, "text");
        byte[] body = "{\"id\":1,\"requesterId\":20,\"recommenderId\":30,\"text\":\"text\"}".getBytes(StandardCharsets.UTF_8);
        byte[] channel = "recommendation-channel".getBytes(StandardCharsets.UTF_8);
        Message message = new DefaultMessage(channel, body);

        doAnswer(invocation -> {
            var consumer = invocation.getArgument(2, java.util.function.Consumer.class);
            consumer.accept(event);
            return null;
        }).when(listener).handleEvent(any(), eq(RecommendationRequestEvent.class), any());

        doThrow(new IllegalArgumentException("No message builder found!"))
                .when(listener).getMessage(event, Locale.UK);

        assertThrows(IllegalArgumentException.class, () -> listener.onMessage(message, null));
    }

    @Test
    void onMessage_WhenUserHasNoPreferredContact_ShouldThrowException() {

        RecommendationRequestEvent event = new RecommendationRequestEvent(
                1L, 20L, 30L, "text");
        byte[] body = "{\"id\":1,\"requesterId\":20,\"recommenderId\":30,\"text\":\"text\"}"
                .getBytes(StandardCharsets.UTF_8);
        byte[] channel = "recommendation-channel".getBytes(StandardCharsets.UTF_8);
        Message message = new DefaultMessage(channel, body);

        doAnswer(invocation -> {
            var consumer = invocation.getArgument(2, java.util.function.Consumer.class);
            consumer.accept(event);
            return null;
        }).when(listener).handleEvent(any(), eq(RecommendationRequestEvent.class), any());

        doReturn("test notification").when(listener).getMessage(event, Locale.UK);

        doThrow(new IllegalArgumentException("no preferred contact method found"))
                .when(listener).sendNotification(30L, "test notification");

        assertThrows(IllegalArgumentException.class, () -> listener.onMessage(message, null));

    }
}
