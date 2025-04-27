package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.enums.RequestStatus;
import faang.school.notificationservice.enums.RequestType;
import faang.school.notificationservice.event.RequestEventEvent;
import faang.school.notificationservice.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestEventEventMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private RequestEventEventMessageBuilder messageBuilder;

    private UserDto userDto;
    private RequestEventEvent eventTodo;
    private RequestEventEvent eventReady;
    private RequestEventEvent eventDone;
    private RequestEventEvent eventCancelled;
    private final LocalDateTime timestamp = LocalDateTime.now();

    @BeforeEach
    public void setUp() {
        userDto = UserDto.builder().username("testUser").build();

        eventTodo = getTestRequestEventEvent(RequestStatus.TODO);
        eventReady = getTestRequestEventEvent(RequestStatus.READY);
        eventDone = getTestRequestEventEvent(RequestStatus.DONE);
        eventCancelled = getTestRequestEventEvent(RequestStatus.CANCELLED);

        when(userServiceClient.getUser(anyLong())).thenReturn(userDto);
    }

    @Test
    public void testBuildMessage_shouldReturnCreatedMessage_whenStatusIsTodo_() {
        // Arrange
        var expectedMessage = "Created message";
        var expectedArgs = new Object[]{userDto.getUsername(), eventTodo.id(), eventTodo.timestamp()};
        when(messageSource.getMessage(eq("request.created"), eq(expectedArgs), eq(Locale.getDefault())))
                .thenReturn(expectedMessage);

        // Act
        var result = messageBuilder.buildMessage(eventTodo, Locale.getDefault());

        // Assert
        assertEquals(expectedMessage, result);
        verify(messageSource).getMessage(eq("request.created"), eq(expectedArgs), eq(Locale.getDefault()));
    }

    @Test
    public void testBuildMessage_shouldReturnReadyMessage_whenStatusIsReady_() {
        // Arrange
        var expectedMessage = "Ready message";
        var expectedArgs = new Object[]{userDto.getUsername(), eventReady.id(), eventReady.timestamp()};
        when(messageSource.getMessage(eq("request.ready"), eq(expectedArgs), eq(Locale.getDefault())))
                .thenReturn(expectedMessage);

        // Act
        var result = messageBuilder.buildMessage(eventReady, Locale.getDefault());

        // Assert
        assertEquals(expectedMessage, result);
        verify(messageSource).getMessage(eq("request.ready"), eq(expectedArgs), eq(Locale.getDefault()));
    }

    @Test
    public void testBuildMessage_shouldReturnDoneMessage_whenStatusIsDone() {
        // Arrange
        var expectedMessage = "Done message";
        var expectedArgs = new Object[]{userDto.getUsername(), eventDone.id(), eventDone.timestamp()};
        when(messageSource.getMessage(eq("request.done"), eq(expectedArgs), eq(Locale.getDefault())))
                .thenReturn(expectedMessage);

        // Act
        var result = messageBuilder.buildMessage(eventDone, Locale.getDefault());

        // Assert
        assertEquals(expectedMessage, result);
        verify(messageSource).getMessage(eq("request.done"), eq(expectedArgs), eq(Locale.getDefault()));
    }

    @Test
    public void testBuildMessage_shouldReturnCancelledMessage_whenStatusIsCancelled() {
        // Arrange
        var expectedMessage = "Cancelled message";
        var expectedArgs = new Object[]{userDto.getUsername(), eventCancelled.id(), eventCancelled.timestamp()};
        when(messageSource.getMessage(eq("request.cancelled"), eq(expectedArgs), eq(Locale.getDefault())))
                .thenReturn(expectedMessage);

        // Act
        var result = messageBuilder.buildMessage(eventCancelled, Locale.getDefault());

        // Assert
        assertEquals(expectedMessage, result);
        verify(messageSource).getMessage(eq("request.cancelled"), eq(expectedArgs), eq(Locale.getDefault()));
    }

    @Test
    public void testBuildMessage_throwUserNotFoundException_whenUserServiceThrowsException() {
        // Arrange
        when(userServiceClient.getUser(anyLong())).thenThrow(new RuntimeException("Service unavailable"));

        assertThrows(UserNotFoundException.class, () ->
                messageBuilder.buildMessage(eventTodo, Locale.getDefault())
        );
    }

    private RequestEventEvent getTestRequestEventEvent(RequestStatus cancelled) {
        return RequestEventEvent.builder()
                .id(UUID.randomUUID())
                .userId(2L)
                .requestType(RequestType.CREATE)
                .requestStatus(cancelled)
                .body(new HashMap<>())
                .timestamp(timestamp)
                .build();
    }
}