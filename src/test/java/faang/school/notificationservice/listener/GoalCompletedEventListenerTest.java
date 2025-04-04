package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserNotificationDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.model.GoalCompletedEvent;
import faang.school.notificationservice.service.NotificationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalCompletedEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private NotificationService notificationService;
    @Mock
    private MessageBuilder<GoalCompletedEvent> messageBuilder;

    private GoalCompletedEventListener listener;

    @BeforeEach
    void setup() {
        Map<UserNotificationDto.PreferredContact, NotificationService> notificationMap = Map.of(
                UserNotificationDto.PreferredContact.EMAIL, notificationService
        );
        Map<Class<?>, MessageBuilder<?>> builderMap = Map.of(
                GoalCompletedEvent.class, messageBuilder
        );

        listener = new GoalCompletedEventListener(
                objectMapper,
                userServiceClient,
                notificationMap,
                builderMap
        );
    }

    @Test
    void shouldHandleValidEvent() throws Exception {
        GoalCompletedEvent event = new GoalCompletedEvent(1L, 123L);
        String json = "{\"userId\":1,\"goalId\":123}";
        Locale locale = new Locale("ru");
        String builtMessage = "Поздравляем! Вы достигли цели 123!";

        UserNotificationDto userDto = new UserNotificationDto();
        userDto.setId(1L);
        userDto.setLocale(locale);
        userDto.setPreference(UserNotificationDto.PreferredContact.EMAIL);

        when(objectMapper.readValue(json, GoalCompletedEvent.class)).thenReturn(event);
        when(userServiceClient.getUserNotificationDto(1L)).thenReturn(userDto);
        when(messageBuilder.buildMessage(event, locale)).thenReturn(builtMessage);

        listener.handleMessage(json);

        verify(notificationService).send(userDto, builtMessage);
    }
}