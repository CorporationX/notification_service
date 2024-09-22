package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEvent;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.listener.comment.CommentEventEventListener;
import faang.school.notificationservice.messaging.comment.CommentEventMessageBuilder;
import faang.school.notificationservice.service.ExampleService;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@ExtendWith(MockitoExtension.class)
public class CommentEventListenerTest {
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private ExampleService exampleService;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private Message message;
    @Mock
    private CommentEventMessageBuilder commentEventMessageBuilder;
    private CommentEventEventListener commentEventListener;
    private List<NotificationService> notificationServices;
    private CommentEvent commentEvent;
    private byte[] bytes;
    private UserDto user;
    private String messageText;
    private Object[] args;

    @BeforeEach
    public void setUp() {
        notificationServices = new ArrayList<>();
        notificationServices.add(exampleService);
        commentEventListener = new CommentEventEventListener(objectMapper, notificationServices, commentEventMessageBuilder, userServiceClient);
        messageText = "new message";
        bytes = messageText.getBytes();
        user = UserDto.builder()
                .id(1L)
                .username("test")
                .email("test@test.com")
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
        commentEvent = CommentEvent.builder()
                .postAuthorId(1L)
                .authorId(2L)
                .postId(3L)
                .commentId(4L)
                .comment("test")
                .build();
        args = commentEventListener.getArgs(commentEvent);
    }

    @Test
    public void testOnMessage() throws IOException {
        Mockito.when(message.getBody()).thenReturn(messageText.getBytes(StandardCharsets.UTF_8));
        Mockito.when(commentEventMessageBuilder.buildMessage(commentEvent, Locale.US, args)).thenReturn("new message");
        Mockito.when(userServiceClient.getUser(1L)).thenReturn(user);
        Mockito.when(notificationServices.get(0).getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);
        Mockito.when(objectMapper.readValue(message.getBody(), CommentEvent.class)).thenReturn(commentEvent);
        Mockito.doNothing().when(notificationServices.get(0)).send(user, messageText);
        commentEventListener.onMessage(message, bytes);
        Mockito.verify(objectMapper, Mockito.times(1)).readValue(message.getBody(), CommentEvent.class);
        Mockito.verify(userServiceClient, Mockito.times(1)).getUser(1L);
        Mockito.verify(exampleService, Mockito.times(1)).send(user, messageText);
    }

}
