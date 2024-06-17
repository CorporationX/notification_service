package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEvent;
import faang.school.notificationservice.dto.UserNotificationDto;
import faang.school.notificationservice.messaging.CommentMessageBuilder;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private CommentMessageBuilder commentMessageBuilder;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private CommentEventListener commentEventListener;

    private CommentEvent commentEvent;
    private UserNotificationDto userNotificationDto;
    private Message message;
    private Class tClass;


    @BeforeEach
    void setUp() {
        objectMapper = mock(ObjectMapper.class);
        commentMessageBuilder = mock(CommentMessageBuilder.class);
        userServiceClient = mock(UserServiceClient.class);
        notificationService = mock(NotificationService.class);
        commentEventListener = new CommentEventListener(objectMapper, userServiceClient,
                List.of(commentMessageBuilder), List.of(notificationService));
        commentEvent = CommentEvent.builder()
                .commentAuthorId(1L)
                .postAuthorId(2L).build();
        userNotificationDto = UserNotificationDto.builder()
                .username("Max")
                .build();
        tClass = commentEvent.getClass();
        message = mock(Message.class);
    }

    @Test
    public void whenOnMessageSuccessfully() throws IOException {
        byte[] body = new byte[0];
        when(message.getBody()).thenReturn(body);
        when(objectMapper.readValue(body, CommentEvent.class)).thenReturn(commentEvent);
        when(commentMessageBuilder.getInstance()).thenReturn(tClass);
        when(commentMessageBuilder.buildMessage(any(), any(), any())).thenReturn("Text");
        when(userServiceClient.getDtoForNotification(anyLong())).thenReturn(userNotificationDto);
        commentEventListener.onMessage(message, body);
        verify(objectMapper).readValue(message.getBody(), CommentEvent.class);
    }
}