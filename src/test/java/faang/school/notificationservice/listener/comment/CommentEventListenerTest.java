package faang.school.notificationservice.listener.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.comment.CommentEvent;
import faang.school.notificationservice.exception.listener.EventHandlingException;
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
import java.util.Collections;
import java.util.Locale;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentEventListenerTest {

    @InjectMocks
    private CommentEventListener commentEventListener;

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private MessageBuilder<CommentEvent> messageBuilder;
    @Mock
    private Message message;
    @Mock
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        commentEventListener = new CommentEventListener(
                objectMapper,
                userServiceClient,
                Collections.singletonList(messageBuilder),
                Collections.singletonList(notificationService));
    }

    @Test
    void testOnMessageShouldThrowException() throws IOException {
        String invalidJsonMessage = "Invalid json";

        when(message.getBody()).thenReturn(invalidJsonMessage.getBytes());
        when(objectMapper.readValue(invalidJsonMessage.getBytes(), CommentEvent.class)).thenThrow(IOException.class);

        assertThrows(EventHandlingException.class, () -> commentEventListener.onMessage(message, new byte[0]));
        verifyNoInteractions(messageBuilder, notificationService);


    }

    @Test
    void testOnMessage() throws IOException {
        UUID eventId = UUID.randomUUID();
        var commentEvent = CommentEvent.builder()
                .uuid(eventId)
                .commentId(1)
                .postId(1)
                .authorId(1)
                .postAuthorId(1)
                .content("Content")
                .build();
        String jsonMessage = String.format(
                "{\"eventId\":\"%s\",\"commentId\":1,\"postId\":1,\"authorId\":1, \"postAuthorId\":1, \"content\":'Content'}",
                eventId
        );

        when(message.getBody()).thenReturn(jsonMessage.getBytes());
        when(objectMapper.readValue(jsonMessage.getBytes(), CommentEvent.class)).thenReturn(commentEvent);

        String notificationContent = "New comment notification";
        when(messageBuilder.getInstance()).thenReturn(CommentEvent.class);
        when(messageBuilder.buildMessage(commentEvent, Locale.US)).thenReturn(notificationContent);

        UserDto userDto = new UserDto();
        userDto.setPreference(UserDto.PreferredContact.EMAIL);
        when(userServiceClient.getUser(1)).thenReturn(userDto);

        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        commentEventListener.onMessage(message, new byte[0]);

        verify(objectMapper).readValue(jsonMessage.getBytes(), CommentEvent.class);
        verify(messageBuilder).getInstance();
        verify(messageBuilder).buildMessage(commentEvent, Locale.US);
        verify(userServiceClient).getUser(1);
        verify(notificationService).getPreferredContact();
        verify(notificationService).send(userDto, notificationContent);
    }
}