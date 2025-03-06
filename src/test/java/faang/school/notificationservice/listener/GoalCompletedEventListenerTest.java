package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.GoalCompletedEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GoalCompletedEventListenerTest {

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MessageBuilder<GoalCompletedEvent> messageBuilder;

    @Mock
    private NotificationService notificationService;

    private GoalCompletedEventListener listener;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        List<MessageBuilder<GoalCompletedEvent>> builders = List.of(messageBuilder);
        List<NotificationService> notificationServices = List.of(notificationService);
        listener = new GoalCompletedEventListener(userServiceClient, builders, notificationServices);
    }

    @Test
    public void testOnMessage() {
        // Создаем тестовое событие
        GoalCompletedEvent event = new GoalCompletedEvent(123L, 456L);
        String builtMessage = "Congratulations! You completed goal 456";

        // Необходимо вернуть класс события, чтобы AbstractEventListener нашел нужный message builder
        when(messageBuilder.getInstance()).thenReturn((Class) GoalCompletedEvent.class);
        when(messageBuilder.buildMessage(event, Locale.UK)).thenReturn(builtMessage);

        UserDto userDto = UserDto.builder()
                .id(123L)
                .username("user123")
                .email("user123@example.com")
                .phone("1234567890")
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
        when(userServiceClient.getUser(123L)).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        // Вызываем обработчик события
        listener.onMessage(event);

        // Проверяем, что message builder и сервисы были вызваны корректно
        verify(messageBuilder).buildMessage(event, Locale.UK);
        verify(userServiceClient).getUser(123L);
        verify(notificationService).send(userDto, builtMessage);
    }
}
