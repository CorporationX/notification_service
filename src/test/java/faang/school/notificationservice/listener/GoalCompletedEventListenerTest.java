package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.GoalCompletedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalCompletedEventListenerTest {
    @InjectMocks
    private GoalCompletedEventListener goalCompletedEventListener;
    @Spy
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private NotificationService notificationService;
    @Mock
    private MessageBuilder<GoalCompletedEvent> messageBuilder;
    @Mock
    private Message message;
    private GoalCompletedEvent goalCompletedEvent;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        List<NotificationService> notifications = new ArrayList<>(List.of(notificationService));
        goalCompletedEventListener = new GoalCompletedEventListener(objectMapper, userServiceClient, notifications, messageBuilder);
        goalCompletedEvent = new GoalCompletedEvent();
        userDto = new UserDto();
        userDto.setId(3L);
        userDto.setLocale(Locale.UK);
    }

    @Test
    void testMapMessage() throws JsonProcessingException {
        goalCompletedEvent.setUserId(1L);
        goalCompletedEvent.setGoalId(2L);

        String json = objectMapper.writeValueAsString(goalCompletedEvent);

        when(message.getBody()).thenReturn(json.getBytes());

        GoalCompletedEvent event = goalCompletedEventListener.mapMessage(message, GoalCompletedEvent.class);
        assertEquals(event.getGoalId(), 2L);
        assertEquals(event.getUserId(), 1L);
    }

    @Test
    void testMapMessageThrowsException() {
        when(message.getBody()).thenReturn("".getBytes());

        assertThrows(IllegalArgumentException.class,
                () -> goalCompletedEventListener.mapMessage(message, GoalCompletedEvent.class));
    }

    @Test
    void testGetMessage() {
        when(userServiceClient.getUser(3L)).thenReturn(userDto);

        goalCompletedEventListener.getMessage(goalCompletedEvent, 3L);

        verify(messageBuilder).buildMessage(goalCompletedEvent, userDto.getLocale());
    }

    @Test
    void testSendMessage() {
        userDto.setPreference(UserDto.PreferredContact.EMAIL);

        when(userServiceClient.getUser(3L)).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        goalCompletedEventListener.sendMessage(3L, "message");

        verify(notificationService).send(userDto, "message");
    }

    @Test
    void testSendMessageThrownException() {
        userDto.setPreference(UserDto.PreferredContact.EMAIL);

        when(userServiceClient.getUser(3L)).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.SMS);

        assertThrows(EntityNotFoundException.class,
                () -> goalCompletedEventListener.sendMessage(3L, "message"));
    }
}
