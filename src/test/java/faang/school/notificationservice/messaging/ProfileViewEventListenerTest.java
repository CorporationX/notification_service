package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.profile.ProfileViewEvent;
import faang.school.notificationservice.listener.ProfileViewEventListener;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static faang.school.notificationservice.dto.UserDto.PreferredContact.TELEGRAM;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfileViewEventListenerTest {

    @InjectMocks
    private ProfileViewEventListener profileViewEventListener;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private List<MessageBuilder<ProfileViewEvent>> messageBuilders;

    @Mock
    private List<NotificationService> notificationServices;

    @Mock
    private NotificationService notificationServices1;

    @Mock
    private Message message;

    @BeforeEach
    public void setUp() {
        MessageBuilder messageBuilder = mock(MessageBuilder.class);
        when(messageBuilder.getInstance()).thenReturn(ProfileViewEvent.class);
        notificationServices = List.of(notificationServices1);
        messageBuilders = List.of(messageBuilder);
        profileViewEventListener = new ProfileViewEventListener(objectMapper, messageBuilders, userServiceClient, notificationServices);
    }

    @Test
    void testOnMessage() throws IOException {
        ProfileViewEvent event = new ProfileViewEvent();
        event.setAuthorId(1L);
        event.setViewerId(2L);
        byte[] body = "word".getBytes();
        String text = "text";

        when(message.getBody()).thenReturn(body);
        when(objectMapper.readValue(body, ProfileViewEvent.class)).thenReturn(event);
        when(messageBuilders.get(0).buildMessage(event, Locale.UK)).thenReturn(text);

        UserDto userDto = new UserDto();
        userDto.setUsername("123");
        userDto.setPreference(TELEGRAM);

        when(userServiceClient.getUser(event.getAuthorId())).thenReturn(userDto);
        when(notificationServices1.getPreferredContact()).thenReturn(UserDto.PreferredContact.TELEGRAM);
        profileViewEventListener.onMessage(message, null);
        verify(notificationServices1, times(1)).send(userDto, text);
    }
}