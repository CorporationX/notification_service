package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.CommentEventMessageBuilder;
import faang.school.notificationservice.service.ExampleService;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
    @Spy
    private CommentEventMessageBuilder commentEventMessageBuilder;
    private MessageSource messageSource;
    private CommentEventListener commentEventListener;
    private List<NotificationService> notificationServices;
    private CommentEvent commentEvent;
    private Byte[] bytes;
    private UserDto user;
    private String messageText;

    @BeforeEach
    public void setUp() {
        notificationServices = new ArrayList<>();
        notificationServices.add(exampleService);
        commentEventMessageBuilder = new CommentEventMessageBuilder(messageSource);
        commentEventListener = new CommentEventListener(objectMapper, notificationServices, commentEventMessageBuilder, userServiceClient);
        bytes = new Byte[]{};
        messageText = "message";
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
    }

    @Test
    public void testOnMessage() throws IOException {
        Mockito.when(message.getBody()).thenReturn(messageText.getBytes(StandardCharsets.UTF_8));
        Mockito.when(userServiceClient.getUser(1L)).thenReturn(user);
        Mockito.when(notificationServices.get(0).getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);
        Mockito.when(objectMapper.readValue(message.getBody(), CommentEvent.class)).thenReturn(commentEvent);
        Mockito.doNothing().when(notificationServices.get(0)).send(user, messageText);


    }

}
