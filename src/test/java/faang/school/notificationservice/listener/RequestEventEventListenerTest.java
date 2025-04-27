package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.enums.RequestStatus;
import faang.school.notificationservice.enums.RequestType;
import faang.school.notificationservice.event.RequestEventEvent;
import faang.school.notificationservice.exception.UserNotFoundException;
import faang.school.notificationservice.messaging.RequestEventEventMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestEventEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private RequestEventEventMessageBuilder messageBuilder;

    @Mock
    private List<NotificationService> notificationServices;

    @Mock
    private Message message;

    @InjectMocks
    private RequestEventEventListener listener;

    private UserDto userDto;
    private RequestEventEvent event;
    private final String builtMessage = "Test message to send";

    @BeforeEach
    public void setUp() {
        userDto = UserDto.builder().username("testUser").preference(UserDto.PreferredContact.EMAIL).build();

        event = RequestEventEvent.builder()
                .id(UUID.randomUUID())
                .userId(2L)
                .requestType(RequestType.CREATE)
                .requestStatus(RequestStatus.TODO)
                .timestamp(java.time.LocalDateTime.now())
                .body(new HashMap<>())
                .build();

        var messageBody = "{\"eventData\":\"testData\"}";
        when(message.getChannel()).thenReturn("channel-name".getBytes());
        when(message.getBody()).thenReturn(messageBody.getBytes());
    }

    @Test
    public void testOnMessage_shouldProcessMessageSuccessfully() throws JsonProcessingException {
        // Arrange
        var mockEmailService = mock(NotificationService.class);
        when(mockEmailService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);
        when(notificationServices.stream()).thenReturn(Stream.of(mockEmailService));

        when(objectMapper.readValue(anyString(), eq(RequestEventEvent.class))).thenReturn(event);
        when(messageBuilder.buildMessage(any(RequestEventEvent.class), any(Locale.class))).thenReturn(builtMessage);
        when(userServiceClient.getUser(anyLong())).thenReturn(userDto);

        // Act
        listener.onMessage(message, "pattern".getBytes());

        // Assert
        verify(objectMapper).readValue(anyString(), eq(RequestEventEvent.class));
        verify(messageBuilder).buildMessage(eq(event), eq(Locale.getDefault()));
        verify(userServiceClient).getUser(eq(event.userId()));
        verify(mockEmailService).send(eq(userDto), eq(builtMessage));
    }

    @Test
    public void testOnMessage_rethrowRuntimeException_whenJsonProcessingExceptionOccurs()
            throws JsonProcessingException {
        when(objectMapper.readValue(anyString(), eq(RequestEventEvent.class)))
                .thenThrow(new JsonProcessingException("Test JSON error") {
                });

        assertThrows(RuntimeException.class, () -> listener.onMessage(message, "pattern".getBytes()));
    }

    @Test
    public void onMessage_shouldHandleException_whenRuntimeExceptionOccurs_() throws JsonProcessingException {
        when(objectMapper.readValue(anyString(), eq(RequestEventEvent.class))).thenReturn(event);
        when(messageBuilder.buildMessage(any(), any()))
                .thenThrow(new RuntimeException("Test runtime exception"));

        assertDoesNotThrow(() -> listener.onMessage(message, "pattern".getBytes()));
    }

    @Test
    public void onMessage_shouldSendNotificationsToMatchingServices() throws JsonProcessingException {
        // Arrange
        var emailService = mock(NotificationService.class);
        var smsService = mock(NotificationService.class);

        when(emailService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);
        when(smsService.getPreferredContact()).thenReturn(UserDto.PreferredContact.PHONE);

        when(objectMapper.readValue(anyString(), eq(RequestEventEvent.class))).thenReturn(event);
        when(messageBuilder.buildMessage(any(RequestEventEvent.class), any(Locale.class))).thenReturn(builtMessage);
        when(userServiceClient.getUser(anyLong())).thenReturn(userDto);

        when(notificationServices.stream()).thenReturn(Stream.of(emailService, smsService));

        // Act
        listener.onMessage(message, "pattern".getBytes());

        // Assert
        verify(emailService).send(eq(userDto), eq(builtMessage));
        verify(smsService, never()).send(any(), any());
    }

    @Test
    public void onMessage_shouldHandleException_whenUserNotFound() throws JsonProcessingException {
        when(objectMapper.readValue(anyString(), eq(RequestEventEvent.class))).thenReturn(event);
        when(messageBuilder.buildMessage(any(RequestEventEvent.class), any(Locale.class))).thenReturn(builtMessage);
        when(userServiceClient.getUser(anyLong())).thenReturn(userDto);
        when(userServiceClient.getUser(anyLong()))
                .thenThrow(new UserNotFoundException("Test user not found"));

        assertDoesNotThrow(() -> listener.onMessage(message, "pattern".getBytes()));
    }
}