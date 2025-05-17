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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestEventEventMessageBuilderTest {

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private RequestEventEventStatusMessageBuilder requestStatusMessageBuilder1;

    @Mock
    private RequestEventEventStatusMessageBuilder requestStatusMessageBuilder2;

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

        messageBuilder = new RequestEventEventMessageBuilder(
                List.of(requestStatusMessageBuilder1, requestStatusMessageBuilder2), userServiceClient);
    }

    @Test
    public void testBuildMessage_shouldReturnCreatedMessage_whenStatusIsTodo_() {
        // Arrange
        var expectedMessage = "Created message";
        var locale = Locale.getDefault();

        when(requestStatusMessageBuilder1.isApplicable(any())).thenReturn(false);

        when(requestStatusMessageBuilder2.isApplicable(eventTodo)).thenReturn(true);
        when(requestStatusMessageBuilder2.buildMessage(eventTodo, userDto, locale)).thenReturn(expectedMessage);

        // Act
        var result = messageBuilder.buildMessage(eventTodo, Locale.getDefault());

        // Assert
        assertEquals(expectedMessage, result);
    }

    @Test
    public void testBuildMessage_shouldReturnReadyMessage_whenStatusIsReady_() {
        // Arrange
        var expectedMessage = "Ready message";
        var locale = Locale.getDefault();

        when(requestStatusMessageBuilder1.isApplicable(any())).thenReturn(false);

        when(requestStatusMessageBuilder2.isApplicable(eventReady)).thenReturn(true);
        when(requestStatusMessageBuilder2.buildMessage(eventReady, userDto, locale)).thenReturn(expectedMessage);

        // Act
        var result = messageBuilder.buildMessage(eventReady, Locale.getDefault());

        // Assert
        assertEquals(expectedMessage, result);
    }

    @Test
    public void testBuildMessage_shouldReturnDoneMessage_whenStatusIsDone() {
        // Arrange
        var expectedMessage = "Done message";
        var locale = Locale.getDefault();

        when(requestStatusMessageBuilder1.isApplicable(any())).thenReturn(false);

        when(requestStatusMessageBuilder2.isApplicable(eventDone)).thenReturn(true);
        when(requestStatusMessageBuilder2.buildMessage(eventDone, userDto, locale)).thenReturn(expectedMessage);

        // Act
        var result = messageBuilder.buildMessage(eventDone, Locale.getDefault());

        // Assert
        assertEquals(expectedMessage, result);
    }

    @Test
    public void testBuildMessage_shouldReturnCancelledMessage_whenStatusIsCancelled() {
        // Arrange
        var expectedMessage = "Cancelled message";
        var locale = Locale.getDefault();

        when(requestStatusMessageBuilder1.isApplicable(any())).thenReturn(false);

        when(requestStatusMessageBuilder2.isApplicable(eventCancelled)).thenReturn(true);
        when(requestStatusMessageBuilder2.buildMessage(eventCancelled, userDto, locale)).thenReturn(expectedMessage);

        // Act
        var result = messageBuilder.buildMessage(eventCancelled, Locale.getDefault());

        // Assert
        assertEquals(expectedMessage, result);
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