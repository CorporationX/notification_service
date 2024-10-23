package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.skill.SkillAcquiredEvent;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.messaging.SkillAcquiredBuilder;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkillAcquiredEventMessageListenerTest {
    private static final long USER_ID = 1L;

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
    private SkillAcquiredBuilder skillAcquiredBuilder;

    private final Map<UserDto.PreferredContact, NotificationService> notificationServices = new HashMap<>();
    private final Map<Class<?>, MessageBuilder<?>> messageBuilders = new HashMap<>();
    private SkillAcquiredEventMessageListener skillAcquiredEventMessageListener;
    private SkillAcquiredEvent skillAcquiredEvent;
    private UserDto userDto;

    @BeforeEach
    void init() {
        skillAcquiredEventMessageListener = new SkillAcquiredEventMessageListener(
                objectMapper,
                userServiceClient,
                messageBuilders,
                notificationServices
        );

        skillAcquiredEvent = skillAcquiredEvent.builder()
                .skillId(1L)
                .receiverId(2L)
                .skillTitle("test")
                .build();

        userDto = UserDto.builder()
                .id(USER_ID)
                .notifyPreference(UserDto.PreferredContact.TELEGRAM)
                .build();
    }

    @Test
    @DisplayName("Should notify user about event")
    void whenCorrectValuesThenNotifyPerson() throws IOException {
        messageBuilders.put(skillAcquiredEvent.getClass(), skillAcquiredBuilder);
        notificationServices.put(UserDto.PreferredContact.TELEGRAM, telegramService);

        when(objectMapper.readValue(message.getBody(), SkillAcquiredEvent.class))
                .thenReturn(skillAcquiredEvent);
        when(userServiceClient.getUser(skillAcquiredEvent.getReceiverId()))
                .thenReturn(userDto);

        when(skillAcquiredBuilder.buildMessage(eq(skillAcquiredEvent), any(Locale.class)))
                .thenReturn(BUILDER_TEXT);

        doNothing().when(telegramService).send(eq(userDto), anyString());

        skillAcquiredEventMessageListener.onMessage(message, new byte[0]);

        verify(objectMapper)
                .readValue(message.getBody(), SkillAcquiredEvent.class);
        verify(userServiceClient)
                .getUser(skillAcquiredEvent.getReceiverId());
        verify(telegramService)
                .send(eq(userDto), anyString());

    }

    @Test
    @DisplayName("Should throw exception when not found any correct message builder")
    void whenNoMessageBuildersThenThrowException() throws IOException {
        when(objectMapper.readValue(message.getBody(), SkillAcquiredEvent.class))
                .thenReturn(skillAcquiredEvent);
        when(userServiceClient.getUser(skillAcquiredEvent.getReceiverId()))
                .thenReturn(userDto);


        assertThrows(NoSuchElementException.class,
                () -> skillAcquiredEventMessageListener.onMessage(message, new byte[0]),
                "Not found message builder");
    }

    @Test
    @DisplayName("Should throw exception when not found any correct preferred notification method")
    void whenNoPreferredNotificationMethodThenThrowException() throws IOException {
        messageBuilders.put(skillAcquiredEvent.getClass(), skillAcquiredBuilder);

        when(objectMapper.readValue(message.getBody(), SkillAcquiredEvent.class))
                .thenReturn(skillAcquiredEvent);
        when(userServiceClient.getUser(skillAcquiredEvent.getReceiverId()))
                .thenReturn(userDto);

        assertThrows(NoSuchElementException.class,
                () -> skillAcquiredEventMessageListener.onMessage(message, new byte[0]),
                "Not found notification service");
    }
}
