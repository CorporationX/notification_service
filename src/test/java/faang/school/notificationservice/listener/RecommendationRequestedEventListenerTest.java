package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.RecommendationRequestedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationRequestedEventListenerTest {

    private final List<MessageBuilder<?>> messageBuilders = new ArrayList<>();
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private NotificationService smsService;
    private RecommendationRequestedEventListener listener;

    private RecommendationRequestedEvent event;
    private UserDto receiverDto;
    private UserDto requesterDto;

    @BeforeEach
    void setUp() {
        event = RecommendationRequestedEvent.builder()
                .receiverId(1L)
                .requesterId(2L)
                .requestId(1L)
                .build();

        receiverDto = UserDto.builder()
                .id(1L)
                .username("ReceiverUser")
                .preference(UserDto.PreferredContact.SMS)
                .build();

        requesterDto = UserDto.builder()
                .id(2L)
                .username("RequesterUser")
                .build();

        lenient().when(smsService.getPreferredContact()).thenReturn(UserDto.PreferredContact.SMS);
        lenient().doNothing().when(smsService).send(eq(receiverDto), anyString());

        List<NotificationService> notificationServices = new ArrayList<>();
        notificationServices.add(smsService);

        listener = spy(new RecommendationRequestedEventListener(objectMapper, userServiceClient, messageBuilders, notificationServices));
    }

    @Test
    @DisplayName("RecommendationRequestedEvent processed successfully")
    void onMessage_SuccessfulProcessing() throws Exception {
        String serializedEvent = "{\"receiverId\":1,\"requesterId\":2,\"requestId\":1}";

        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(serializedEvent.getBytes(StandardCharsets.UTF_8));

        when(objectMapper.readValue(eq(serializedEvent.getBytes(StandardCharsets.UTF_8)), eq(RecommendationRequestedEvent.class)))
                .thenReturn(event);

        when(userServiceClient.getUser(1L)).thenReturn(receiverDto);
        when(userServiceClient.getUser(2L)).thenReturn(requesterDto);

        MessageBuilder<RecommendationRequestedEvent> mockMessageBuilder = mock(MessageBuilder.class);
        when(mockMessageBuilder.isEventTypeSupported(any(RecommendationRequestedEvent.class))).thenReturn(true);

        String expectedMessage = String.format("Hey, %s! %s just requested a recommendation from you! Recommendation ID: %s",
                receiverDto.getUsername(),
                requesterDto.getUsername(),
                event.getRequestId());

        when(mockMessageBuilder.buildMessage(eq(event), any(Locale.class), (Object[]) any())).thenReturn(expectedMessage);

        messageBuilders.add(mockMessageBuilder);

        listener.onMessage(message, null);

        verify(userServiceClient, times(2)).getUser(1L);
        verify(userServiceClient, times(1)).getUser(2L);

        verify(listener, times(1)).sendNotification(eq(receiverDto.getId()), eq(expectedMessage));
        verify(smsService, times(1)).send(eq(receiverDto), eq(expectedMessage));
    }

    @Test
    @DisplayName("RecommendationRequestedEvent deserialization processing")
    void onMessage_DeserializationError() throws Exception {
        String invalidSerializedEvent = "invalid json";

        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(invalidSerializedEvent.getBytes(StandardCharsets.UTF_8));

        when(objectMapper.readValue(eq(invalidSerializedEvent.getBytes(StandardCharsets.UTF_8)),
                eq(RecommendationRequestedEvent.class))).thenThrow(new IOException("Deserialization error"));

        RecommendationRequestedEventListener spyListener = spy(listener);

        spyListener.onMessage(message, null);

        verify(spyListener, never()).sendNotification(anyLong(), anyString());

        verify(spyListener, never()).getMessage(any(), any(), any());
    }

    @Test
    @DisplayName("RecommendationRequestedEvent with non-existing user processing")
    void onMessage_UserNotFound() throws Exception {
        String serializedEvent = "{\"receiverId\":1,\"requesterId\":2,\"requestId\":1}";

        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(serializedEvent.getBytes(StandardCharsets.UTF_8));

        when(objectMapper.readValue(eq(serializedEvent.getBytes(StandardCharsets.UTF_8)),
                eq(RecommendationRequestedEvent.class))).thenReturn(event);

        when(userServiceClient.getUser(1L)).thenReturn(receiverDto);
        when(userServiceClient.getUser(2L)).thenThrow(new RuntimeException("User not found"));

        RecommendationRequestedEventListener spyListener = spy(listener);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            spyListener.onMessage(message, null);
        });
        assertTrue(exception.getMessage().contains("User not found"));

        verify(userServiceClient, times(1)).getUser(1L);
        verify(userServiceClient, times(1)).getUser(2L);

        verify(spyListener, never()).sendNotification(anyLong(), anyString());
        verify(spyListener, never()).getMessage(any(), any(), any());
    }
}
