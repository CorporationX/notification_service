package faang.school.notificationservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.exception.EventProcessingException;
import faang.school.notificationservice.feign.UserServiceClient;
import faang.school.notificationservice.listener.RecommendationRequestedEventListener;
import faang.school.notificationservice.model.dto.RecommendationRequestDto;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.RecommendationRequestedEvent;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.impl.RecommendationRequestedMessageBuilder;
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
public class RecommendationRequestedEventListenerTest {

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

    private RecommendationRequestedEventListener listener;

    @BeforeEach
    public void setUp() {
        List<NotificationService> notificationServices = Collections.singletonList(notificationService);
        List<MessageBuilder<?>> messageBuilders = Collections.singletonList(
                new RecommendationRequestedMessageBuilder(userServiceClient, messageSource));

        listener = new RecommendationRequestedEventListener(objectMapper, userServiceClient,
                notificationServices, messageBuilders);
    }

    @Test
    @DisplayName("Should successfully process RecommendationRequestedEvent and send notification")
    public void testOnMessage_Success() throws Exception {
        RecommendationRequestedEvent event = new RecommendationRequestedEvent();
        event.setRequesterId(1L);
        event.setReceiverId(3L);
        event.setRequestId(1L);

        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(message.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, RecommendationRequestedEvent.class)).thenReturn(event);

        UserDto receiverDto = new UserDto();
        receiverDto.setId(3L);
        receiverDto.setUsername("Recommendation receiver user");

        when(userServiceClient.getUser(3L)).thenReturn(receiverDto);
        when(userServiceClient.getUser(1L)).thenReturn(new UserDto());
        when(userServiceClient.getRecommendationRequest(1L)).thenReturn(new RecommendationRequestDto());
        when(messageSource.getMessage(eq("recommendation.requested"), any(), eq(Locale.UK))).thenReturn("Notification message");

        listener.onMessage(message, null);

        verify(notificationService, times(1)).send(eq(receiverDto), eq("Notification message"));
    }

    @Test
    @DisplayName("Should throw EventProcessingException when message parsing fails")
    public void testOnMessage_EventProcessingException() throws Exception {
        byte[] messageBody = new byte[0];
        when(message.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, RecommendationRequestedEvent.class))
                .thenThrow(new IOException("Error parsing"));

        Executable executable = () -> listener.onMessage(message, new byte[0]);

        EventProcessingException exception = assertThrows(EventProcessingException.class, executable);
        assertEquals("Failed to process event of type RecommendationRequestedEvent", exception.getMessage());
    }
}