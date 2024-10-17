package faang.school.notificationservice.listener.goal;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.goal.GoalCompletedEvent;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messaging.goal.GoalCompletedEventBuilder;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.telegram.TelegramService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalCompletedEventListenerTest {

    private static final long GOAL_ID = 1L;
    private static final long USER_ID = 1L;

    private static final String GOAL_NAME = "TEST";
    private static final String BUILDER_TEXT = "BUILDER_TEXT";

    @Mock
    private Message message;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private TelegramService telegramService;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private GoalCompletedEventBuilder goalCompletedEventBuilder;

    private final Map<UserDto.PreferredContact, NotificationService> notificationServices = new HashMap<>();
    private final Map<Class<?>, MessageBuilder<?>> messageBuilders = new HashMap<>();
    private GoalCompletedEventListener goalCompletedEventListener;
    private GoalCompletedEvent goalCompletedEvent;
    private UserDto userDto;

    @BeforeEach
    void init() {
        goalCompletedEventListener = new GoalCompletedEventListener(
                objectMapper,
                userServiceClient,
                messageBuilders,
                notificationServices
        );

        goalCompletedEvent = GoalCompletedEvent.builder()
                .goalId(GOAL_ID)
                .userId(USER_ID)
                .goalName(GOAL_NAME)
                .build();

        userDto = UserDto.builder()
                .id(USER_ID)
                .notifyPreference(UserDto.PreferredContact.TELEGRAM)
                .build();
    }

    @Test
    @DisplayName("Should notify user about event")
    void whenCorrectValuesThenNotifyPerson() throws IOException {
        messageBuilders.put(goalCompletedEvent.getClass(), goalCompletedEventBuilder);
        notificationServices.put(UserDto.PreferredContact.TELEGRAM, telegramService);

        when(objectMapper.readValue(message.getBody(), GoalCompletedEvent.class))
                .thenReturn(goalCompletedEvent);
        when(userServiceClient.getUser(goalCompletedEvent.getUserId()))
                .thenReturn(userDto);

        when(goalCompletedEventBuilder.buildMessage(eq(goalCompletedEvent), any(Locale.class)))
                .thenReturn(BUILDER_TEXT);

        doNothing().when(telegramService).send(eq(userDto), anyString());

        goalCompletedEventListener.onMessage(message, new byte[0]);

        verify(objectMapper)
                .readValue(message.getBody(), GoalCompletedEvent.class);
        verify(userServiceClient)
                .getUser(goalCompletedEvent.getUserId());
        verify(telegramService)
                .send(eq(userDto), anyString());

    }

    @Test
    @DisplayName("Should throw exception when not found any correct message builder")
    void whenNoMessageBuildersThenThrowException() throws IOException {
        when(objectMapper.readValue(message.getBody(), GoalCompletedEvent.class))
                .thenReturn(goalCompletedEvent);
        when(userServiceClient.getUser(goalCompletedEvent.getUserId()))
                .thenReturn(userDto);


        assertThrows(NoSuchElementException.class,
                () -> goalCompletedEventListener.onMessage(message, new byte[0]),
                "Not found message builder");

    }

    @Test
    @DisplayName("Should throw exception when not found any correct preferred notification method")
    void whenNoPreferredNotificationMethodThenThrowException() throws IOException {
        messageBuilders.put(goalCompletedEvent.getClass(), goalCompletedEventBuilder);

        when(objectMapper.readValue(message.getBody(), GoalCompletedEvent.class))
                .thenReturn(goalCompletedEvent);
        when(userServiceClient.getUser(goalCompletedEvent.getUserId()))
                .thenReturn(userDto);

        assertThrows(NoSuchElementException.class,
                () -> goalCompletedEventListener.onMessage(message, new byte[0]),
                "Not found notification service");

    }


}


