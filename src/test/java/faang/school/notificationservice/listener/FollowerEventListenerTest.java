package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.builder.MessageBuilder;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FollowerEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private NotificationService notificationService;
    @Mock
    private MessageBuilder<FollowerEvent> messageBuilder;
    @InjectMocks
    private FollowerEventListener followerEventListener;

    private FollowerEvent followerEvent;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        followerEventListener = new FollowerEventListener(objectMapper, userServiceClient,
                List.of(notificationService), List.of(messageBuilder));

        followerEvent = FollowerEvent.builder()
                .follower(1L)
                .followee(2L)
                .build();

        userDto = UserDto.builder()
                .id(1L)
                .username("username")
                .email("email@gmail.com")
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
    }

    @Test
    public void testOnMessage_Success() throws IOException {
        Message message = mock(Message.class);
        String body = "{\"actorId\":1,\"receiverId\":2,\"requestId\":3}";
        String channel = "follower_channel";

        when(message.getBody()).thenReturn(body.getBytes());
        when(message.getChannel()).thenReturn(channel.getBytes());
        when(objectMapper.readValue(body.getBytes(), FollowerEvent.class)).thenReturn(followerEvent);
        when(userServiceClient.getUser(1L)).thenReturn(userDto);
        doReturn(FollowerEvent.class).when(messageBuilder).supportsEventType();
        when(messageBuilder.buildMessage(any(FollowerEvent.class))).thenReturn("Test message");
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        doNothing().when(notificationService).send(any(UserDto.class), anyString());

        followerEventListener.onMessage(message, new byte[]{});

        verify(objectMapper).readValue(any(byte[].class), eq(FollowerEvent.class));
        verify(userServiceClient).getUser(1L);
        verify(notificationService).send(any(UserDto.class), ArgumentMatchers.eq("Test message"));
    }
}
