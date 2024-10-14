package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.FollowerEvent;
import faang.school.notificationservice.messaging.FollowerEventBuilder;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;

@ExtendWith(MockitoExtension.class)
public class FollowerEventListenerTest {
    @InjectMocks
    private FollowerEventListener eventListener;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private FollowerEventBuilder eventBuilder;
    @Mock
    private Message message;
    @Mock
    private NotificationService notificationService;

    private String jsonFollowerEvent;
    private UserDto user;
    private FollowerEvent event;
    private Locale locale = Locale.getDefault();


    @BeforeEach
    void setup() {
        jsonFollowerEvent = "{\"followerId\": 1, \"followedId\": 2, \"followerName\": 'Mike'}";
        user = new UserDto();
        user.setId(2L);
        user.setEmail("example@mail.com");
        user.setPreference(UserDto.PreferredContact.EMAIL);
        eventListener = new FollowerEventListener(objectMapper, userServiceClient, eventBuilder, List.of(notificationService));
        event = new FollowerEvent(1L, 2L, "Mike");
    }

    @Test
    void testOnMessage() throws IOException {
        String sendMessage = "Congrats! You've got a new follower Mike!";
        when(message.getBody()).thenReturn(jsonFollowerEvent.getBytes());
        when(objectMapper.readValue(jsonFollowerEvent.getBytes(), FollowerEvent.class)).thenReturn(event);
        when(eventBuilder.buildMessage(event, locale)).thenReturn(sendMessage);
        when(userServiceClient.getUser(anyLong())).thenReturn(user);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        eventListener.onMessage(message, new byte[0]);

        verify(objectMapper).readValue(message.getBody(), FollowerEvent.class);
        verify(eventBuilder).buildMessage(event, locale);
        verify(userServiceClient).getUser(user.getId());
        verify(notificationService).send(user, sendMessage);
    }
}
