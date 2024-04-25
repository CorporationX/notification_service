package faang.school.notificationservice.listeners;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikeEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messages.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@ExtendWith(MockitoExtension.class)
class LikeEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private Message message;
    @Mock
    private Consumer<LikeEvent> consumer;
    @Mock
    private NotificationService notificationService;
    @Mock
    private MessageBuilder<Class<?>> messageBuilder;
    @Mock
    private UserServiceClient userServiceClient;
    @InjectMocks
    private LikeEventListener likeEventListener;

    @Mock
    private LikeEventListener likeEventListenerMock;


    private LikeEvent likeEventPost;
    public UserDto userDto;
    private Locale locale;
    private String textMessage;
    private String sendMessage;
    byte[] pattern;

    @BeforeEach
    public void setUp() {
        likeEventListener = new LikeEventListener(objectMapper,
                                                  userServiceClient,
                                                  List.of(notificationService),
                                                  List.of(messageBuilder));

        userDto = UserDto.builder()
                .id(100L)
                .username("Ivan")
                .preference(UserDto.PreferredContact.TELEGRAM)
                .build();

        likeEventPost = LikeEvent.builder()
                .authorLikeId(userDto.getId())
                .authorPostId(200L)
                .postId(300L)
                .build();

        locale = Locale.UK;
        textMessage = "Test";
        sendMessage = "Message!";
        pattern = new byte[0];
    }

    @Test
    @DisplayName("Checking that the method is called with the correct arguments and successfully deserializes the object")
    public void handleEvent_SuccessfulDeserializeEvent() throws IOException {
        when(objectMapper.readValue(message.getBody(), LikeEvent.class)).thenReturn(likeEventPost);

        likeEventListener.handleEvent(message, LikeEvent.class, consumer);

        verify(objectMapper).readValue(message.getBody(), LikeEvent.class);
        verify(consumer).accept(likeEventPost);
    }

    @Test
    @DisplayName("Checking that the method throws an exception during unsuccessful deserialization")
    public void handleEvent_FailedDeserializationEvent() throws IOException {
        when(objectMapper.readValue(message.getBody(), LikeEvent.class)).thenThrow(new IOException());
        assertThrows(RuntimeException.class, () -> likeEventListener.handleEvent(message, LikeEvent.class, consumer));
    }

    @Test
    @DisplayName("Checking that the MessageBuilder exists and the method returns the expected message")
    public void getMessage_MessageBuilderExists() {
        when(messageBuilder.supportsEventType(LikeEvent.class)).thenReturn(true);
        when(messageBuilder.buildMessage(LikeEvent.class, locale)).thenReturn(textMessage);

        String textMessage = likeEventListener.getMessage(LikeEvent.class, locale);
        assertEquals("Test", textMessage);
    }

    @Test
    @DisplayName("Checking that the message builder has not been found and the method throws an exception")
    public void getMessage_MessageBuilderNotFound_ThrowsException() {
        when(messageBuilder.supportsEventType(LikeEvent.class)).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> likeEventListener.getMessage(LikeEvent.class, locale));
    }

    @Test
    @DisplayName("Checking that the user has a preferred contact and a message is sent to this contact")
    public void sendNotification_UserContactExists_MessageSent() {
        when(userServiceClient.getUser(likeEventPost.getAuthorLikeId())).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(userDto.getPreference());

        likeEventListener.sendNotification(likeEventPost.getAuthorLikeId(), sendMessage);

        verify(notificationService).send(userDto, sendMessage);
    }

    @Test
    @DisplayName("Checking that the user's preferred contact was not found and an exception is thrown")
    public void sendNotification_UserContactNotFound_ExceptionThrown() {
        when(userServiceClient.getUser(likeEventPost.getAuthorLikeId())).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        assertThrows(IllegalArgumentException.class,
                () -> likeEventListener.sendNotification(100L, textMessage));
    }

    @Test
    @DisplayName("Checking that the method is called with the correct arguments")
    public void testOnMessage(){
        likeEventListenerMock.onMessage(message, pattern);

        verify(likeEventListenerMock).onMessage(message, pattern);
    }
}