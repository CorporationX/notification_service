package faang.school.notificationservice.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.enums.PreferredContact;
import faang.school.notificationservice.messages.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private Message message;
    @Mock
    private Consumer<CommentEvent> consumer;
    @Mock
    private NotificationService notificationService;
    @Mock
    private MessageBuilder<CommentEvent> messageBuilder;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private CommentEventListener commentEventListenerMock;
    @InjectMocks
    private CommentEventListener commentEventListener;

    private CommentEvent commentEvent;
    public UserDto userDto;
    private Locale locale;
    private String textMessage;
    private String sendMessage;
    byte[] pattern;

    @BeforeEach
    public void setUp() {
        commentEventListener = new CommentEventListener(objectMapper,
                userServiceClient,
                messageBuilder,
                List.of(notificationService));

        userDto = UserDto.builder()
                .id(100L)
                .username("Ivan")
                .preference(PreferredContact.TELEGRAM)
                .build();

        commentEvent = CommentEvent.builder()
                .authorOfCommentId(100L)
                .authorOfPostId(userDto.getId())
                .postId(200L)
                .build();

        locale = Locale.UK;
        textMessage = "some text";
        sendMessage = "Message!";
        pattern = new byte[0];
    }

    @Test
    @DisplayName("Checking that the method is called with the correct arguments and successfully deserializes the object")
    public void handleEvent_SuccessfulDeserializeEvent() throws IOException {
        when(objectMapper.readValue(message.getBody(), CommentEvent.class)).thenReturn(commentEvent);

        commentEventListener.handleEvent(message, CommentEvent.class, consumer);

        verify(objectMapper).readValue(message.getBody(), CommentEvent.class);
        verify(consumer).accept(commentEvent);
    }

    @Test
    @DisplayName("Checking that the method throws an exception during unsuccessful deserialization")
    public void handleEvent_FailedDeserializationEvent() throws IOException {
        when(objectMapper.readValue(message.getBody(), CommentEvent.class)).thenThrow(new IOException());
        assertThrows(RuntimeException.class, () -> commentEventListener.handleEvent(message, CommentEvent.class, consumer));
    }

    @Test
    @DisplayName("Checking that the MessageBuilder exists and the method returns the expected message")
    public void getMessage_MessageBuilderExists() {
        when(messageBuilder.buildMessage(commentEvent, locale)).thenReturn(textMessage);

        String textMessage = commentEventListener.getMessage(commentEvent, locale);
        assertEquals("some text", textMessage);
    }

    @Test
    @DisplayName("Checking that the user has a preferred contact and a message is sent to this contact")
    public void sendNotification_UserContactExists_MessageSent() {
        when(userServiceClient.getUser(commentEvent.getAuthorOfPostId())).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(userDto.getPreference());

        commentEventListener.sendNotification(commentEvent.getAuthorOfPostId(), sendMessage);

        verify(notificationService).send(userDto, sendMessage);
    }

    @Test
    @DisplayName("Checking that the user's preferred contact was not found and an exception is thrown")
    public void sendNotification_UserContactNotFound_ExceptionThrown() {
        when(userServiceClient.getUser(commentEvent.getAuthorOfPostId())).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(PreferredContact.EMAIL);

        assertThrows(IllegalArgumentException.class,
                () -> commentEventListener.sendNotification(100L, textMessage));
    }

    @Test
    @DisplayName("Checking that the method is called with the correct arguments")
    public void testOnMessage(){
        commentEventListenerMock.onMessage(message, pattern);

        verify(commentEventListenerMock).onMessage(message, pattern);
    }
}
