package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.CommentEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.telegram.TelegramService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    MessageBuilder<CommentEvent> messageBuilder;

    @Mock
    private List<NotificationService> notifications;

    @Mock
    private Message message;

    @InjectMocks
    private CommentEventListener commentEventListener;

    @Mock
    private TelegramService telegramService;

    private CommentEvent event;
    private UserDto user;

    @BeforeEach
    void setup() {
        event = CommentEvent.builder()
                .commentAuthorId(1)
                .username("username")
                .postAuthorId(2)
                .postId(3)
                .content("content")
                .commentId(2)
                .build();

        user = UserDto.builder()
                .id(1668746328L)
                .username("Viktor")
                .preference(UserDto.PreferredContact.TELEGRAM)
                .build();
        notifications = List.of(telegramService);
        commentEventListener = new CommentEventListener(objectMapper, userServiceClient,
                messageBuilder, notifications);
    }

    @Test
    void testOnMessageOk() throws IOException{
        var messageBody = "Hello World!".getBytes();

        doReturn(messageBody).when(message).getBody();
        doReturn(event).when(objectMapper).readValue(messageBody, CommentEvent.class);
        doReturn("some message").when(messageBuilder).buildMessage(any(CommentEvent.class), any(Locale.class));
        doReturn(user).when(userServiceClient).getUser(anyLong());
        doNothing().when(telegramService).send(any(UserDto.class), anyString());
        doReturn(UserDto.PreferredContact.TELEGRAM).when(telegramService).getPreferredContact();

        commentEventListener.onMessage(message, new byte[0]);

        verify(objectMapper).readValue(message.getBody(), CommentEvent.class);
        verify(messageBuilder).buildMessage(any(), any());
        verify(userServiceClient).getUser(anyLong());
    }
}