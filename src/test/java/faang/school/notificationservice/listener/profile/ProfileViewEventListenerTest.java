package faang.school.notificationservice.listener.profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.profile.ProfileViewEvent;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.messaging.profile.ProfileViewMessageBuilder;
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
public class ProfileViewEventListenerTest {

    private static final long USER_ID_ONE = 1L;
    private static final String TEST_TEXT = "TEST_TEXT";

    @Mock
    private Message message;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private TelegramService telegramService;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private ProfileViewMessageBuilder profileViewMessageBuilder;

    private final Map<UserDto.PreferredContact, NotificationService> notificationServices = new HashMap<>();
    private final Map<Class<?>, MessageBuilder<?>> messageBuilders = new HashMap<>();
    private ProfileViewEventListener profileViewEventListener;
    private ProfileViewEvent profileViewEvent;
    private UserDto userDto;

    @BeforeEach
    void init() {
        profileViewEventListener = new ProfileViewEventListener(
                objectMapper,
                userServiceClient,
                messageBuilders,
                notificationServices
        );

        profileViewEvent = ProfileViewEvent.builder()
                .userViewedId(USER_ID_ONE)
                .profileViewedId(USER_ID_ONE)
                .build();

        userDto = UserDto.builder()
                .id(USER_ID_ONE)
                .notifyPreference(UserDto.PreferredContact.TELEGRAM)
                .build();
    }

    @Test
    @DisplayName("When valid dto passed then notify user by telegram")
    void whenValidEventPassedThenNotifyPersonByTelegram() throws IOException {
        messageBuilders.put(profileViewEvent.getClass(), profileViewMessageBuilder);
        notificationServices.put(UserDto.PreferredContact.TELEGRAM, telegramService);
        when(objectMapper.readValue(message.getBody(), ProfileViewEvent.class))
                .thenReturn(profileViewEvent);
        when(userServiceClient.getUser(profileViewEvent.getUserViewedId()))
                .thenReturn(userDto);
        when(profileViewMessageBuilder.buildMessage(eq(profileViewEvent), any(Locale.class)))
                .thenReturn(TEST_TEXT);
        doNothing().when(telegramService).send(eq(userDto), anyString());

        profileViewEventListener.onMessage(message, new byte[0]);

        verify(objectMapper)
                .readValue(message.getBody(), ProfileViewEvent.class);
        verify(userServiceClient)
                .getUser(profileViewEvent.getUserViewedId());
        verify(telegramService)
                .send(eq(userDto), anyString());
    }

    @Test
    @DisplayName("When no valid message builder given then throw exception")
    void whenNoMessageBuilderFoundThenThrowException() throws IOException {
        when(objectMapper.readValue(message.getBody(), ProfileViewEvent.class))
                .thenReturn(profileViewEvent);
        when(userServiceClient.getUser(profileViewEvent.getUserViewedId()))
                .thenReturn(userDto);

        assertThrows(NoSuchElementException.class,
                () -> profileViewEventListener.onMessage(message, new byte[0]));
    }

    @Test
    @DisplayName("When no preferred notification method is given then throw exception")
    void whenNoPreferredNotificationMethodIsGivenThenThrowException() throws IOException {
        messageBuilders.put(profileViewEvent.getClass(), profileViewMessageBuilder);
        when(objectMapper.readValue(message.getBody(), ProfileViewEvent.class))
                .thenReturn(profileViewEvent);
        when(userServiceClient.getUser(profileViewEvent.getUserViewedId()))
                .thenReturn(userDto);

        assertThrows(NoSuchElementException.class,
                () -> profileViewEventListener.onMessage(message, new byte[0]));
    }
}
