package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageClient;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.CommentMessageBuilder;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.sms.SmsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentEventListenerTest {

    private List<NotificationService> notificationServices;

    private List<MessageBuilder<CommentEvent>> messageBuilders;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private ObjectMapper objectMapper;

    private CommentEventListener commentEventListener;

    private Message message;

    private CommentEvent commentEvent;

    private MessageBuilder<CommentEvent> messageBuilder;

    @Mock
    private NotificationService notificationService;

    @Mock
    private MessageSource messageSource;

    @Mock
    private VonageClient vonageClient;

    @Mock
    private SmsClient smsClient;

    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        message = new DefaultMessage("comment_channel".getBytes(),
                "{\"id\":1,\"authorId\":123,\"postId\":456,\"postAuthorId\":789,\"content\":\"This is a comment.\"}"
                        .getBytes()
        );
        commentEvent = new CommentEvent(1, 123, 456, 789, "This is a comment");
        userDto = new UserDto(1, "johndoe", "john@gmail.com",
                "996999060631", UserDto.PreferredContact.SMS);
        messageBuilder = new CommentMessageBuilder(messageSource, userServiceClient);
        notificationService = new SmsService(vonageClient);
        notificationServices = Collections.singletonList(notificationService);
        messageBuilders = Collections.singletonList(messageBuilder);
        commentEventListener = new CommentEventListener(notificationServices, messageBuilders,
                userServiceClient, objectMapper);
    }

    @Test
    public void testHandleCommentEventWithError() throws IOException {
        Message message = new DefaultMessage("testChannel".getBytes(), "Test".getBytes());
        when(objectMapper.readValue(message.getBody(), CommentEvent.class)).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class,
                () -> commentEventListener.handleEvent(message, CommentEvent.class));
    }

    @Test
    public void testHandleCommentEventSuccess() throws IOException {
        when(objectMapper.readValue(message.getBody(), CommentEvent.class)).thenReturn(commentEvent);

        commentEvent = commentEventListener.handleEvent(message, CommentEvent.class);

        verify(objectMapper).readValue(message.getBody(), CommentEvent.class);
        assertNotNull(commentEvent);
        assertEquals(1, commentEvent.getId());
        assertEquals(123, commentEvent.getAuthorId());
        assertEquals(456, commentEvent.getPostId());
        assertEquals("This is a comment", commentEvent.getContent());
    }

    @Test
    public void testGetMessageWithNotExistingMessageBuilder() {
        messageBuilders = Collections.emptyList();
        commentEventListener = new CommentEventListener(notificationServices, messageBuilders,
                userServiceClient, objectMapper);

        assertThrows(IllegalArgumentException.class,
                () -> commentEventListener.getMessage(commentEvent, Locale.CANADA));
    }

    @Test
    public void testGetMessageSuccess() {
        String expectedMessage = "This is a comment";
        when(userServiceClient.getUser(anyLong())).thenReturn(userDto);
        when(messageBuilder.buildMessage(commentEvent, Locale.CANADA)).thenReturn(expectedMessage);

        String result = commentEventListener.getMessage(commentEvent, Locale.CANADA);

        assertEquals(expectedMessage, result);
    }

    @Test
    public void testSendNotificationWithNotExistNotificationService() {
        notificationServices = Collections.emptyList();
        commentEventListener = new CommentEventListener(notificationServices, messageBuilders,
                userServiceClient, objectMapper);
        when(userServiceClient.getUser(anyLong())).thenReturn(userDto);

        assertThrows(IllegalArgumentException.class,
                () -> commentEventListener.sendNotification(userDto.getId(), "message"));
    }

    @Test
    public void testSendNotificationSuccess() {
        String message = "This is a comment";
        SmsSubmissionResponse response = new SmsSubmissionResponse();
        when(userServiceClient.getUser(anyLong())).thenReturn(userDto);
        when(vonageClient.getSmsClient()).thenReturn(smsClient);
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);

        commentEventListener.sendNotification(userDto.getId(), message);
        ArgumentCaptor<TextMessage> captorMessage = ArgumentCaptor.forClass(TextMessage.class);

        verify(smsClient).submitMessage(captorMessage.capture());
        verify(vonageClient).getSmsClient();
    }
}
