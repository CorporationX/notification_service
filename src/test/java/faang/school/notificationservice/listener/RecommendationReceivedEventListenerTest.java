package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.exception.EventProcessingException;
import faang.school.notificationservice.feign.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.RecommendationReceivedEvent;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.impl.RecommendationReceivedMessageBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecommendationReceivedEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private NotificationService notificationService;

    @Mock
    private MessageSource messageSource;

    @Mock
    private Message message;

    private RecommendationReceivedEventListener listener;

    @BeforeEach
    public void setUp() {
        List<NotificationService> notificationServices = Collections.singletonList(notificationService);
        List<MessageBuilder<?>> messageBuilders = Collections.singletonList(
                new RecommendationReceivedMessageBuilder(userServiceClient, messageSource));

        listener = new RecommendationReceivedEventListener(objectMapper, userServiceClient,
                notificationServices, messageBuilders);
    }

    @Test
    @DisplayName("Should successfully process RecommendationReceivedEvent and send notification")
    public void testOnMessage_Success() throws Exception {
        RecommendationReceivedEvent event = new RecommendationReceivedEvent();
        event.setAuthorId(1L);
        event.setReceiverUserId(3L);
        event.setRecommendationId(1L);

        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(message.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, RecommendationReceivedEvent.class)).thenReturn(event);

        UserDto receiverUserDto = new UserDto();
        receiverUserDto.setId(3L);
        receiverUserDto.setUsername("Receiver user");

        when(userServiceClient.getUser(3L)).thenReturn(receiverUserDto);
        when(userServiceClient.getUser(1L)).thenReturn(new UserDto());
        when(messageSource.getMessage(eq("recommendation.received"), any(), eq(Locale.UK))).thenReturn("Notification message");

        listener.onMessage(message, null);

        verify(notificationService, times(1)).send(eq(receiverUserDto), eq("Notification message"));
    }

    @Test
    @DisplayName("Should throw EventProcessingException when message parsing fails")
    public void testOnMessage_EventProcessingException() throws Exception {
        byte[] messageBody = new byte[0];
        when(message.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, RecommendationReceivedEvent.class))
                .thenThrow(new IOException("Error parsing"));

        Executable executable = () -> listener.onMessage(message, new byte[0]);

        EventProcessingException exception = assertThrows(EventProcessingException.class, executable);
        assertEquals("Failed to process event of type RecommendationReceivedEvent", exception.getMessage());
    }
}