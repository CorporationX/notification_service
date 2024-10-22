package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ProfileViewEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.Locale;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfileViewEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private NotificationService notificationService;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private MessageBuilder<ProfileViewEvent> messageBuilder;
    @InjectMocks
    private ProfileViewEventListener profileViewEventListener;
    private String jsonEvent;
    private UserDto profileAuthor;
    private ProfileViewEvent event;
    private Message message;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        jsonEvent = "{\"authorId\": \"12345\"}";
        profileAuthor = new UserDto();
        profileAuthor.setLocale(new Locale("en", "US"));
        message = new Message() {
            @Override public byte[] getBody() {
                return jsonEvent.getBytes();
            }

            @Override public byte[] getChannel() {
                return new byte[0];
            }
        };
        profileAuthor = new UserDto();
        profileAuthor.setLocale(new Locale("en", "US"));
        event = new ProfileViewEvent();
        event.setAuthorId(1L);
        event.setViewerId(2L);
    }

    @Test
    void onMessageTest() throws IOException {

        when(objectMapper.readValue(message.getBody(), ProfileViewEvent.class)).thenReturn(event);
        when(userServiceClient.getUser(event.getAuthorId())).thenReturn(profileAuthor);

        profileViewEventListener.onMessage(message, null);

        verify(notificationService, times(1)).send(profileAuthor, messageBuilder.buildMessage(profileAuthor,event));
    }
}
