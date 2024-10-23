package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.LikeEvent;
import faang.school.notificationservice.messaging.LikeMessageBuilder;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LikeEventListenerTest {

    private List<NotificationService> notificationServices;

    private List<MessageBuilder<LikeEvent>> messageBuilders;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private ObjectMapper objectMapper;

    private LikeEventListener likeEventListener;

    private Message message;
    private LikeEvent likeEvent;
    private MessageBuilder<LikeEvent> messageBuilder;

    @Mock
    private NotificationService notificationService;

    @Mock
    private MessageSource messageSource;

    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        message = new DefaultMessage("like_channel".getBytes(),
                "{\"postAuthorId\":1,\"likeAuthorId\":5,\"postId\":10.\"}".getBytes());
        likeEvent = new LikeEvent(1, 5, 10);
        userDto = new UserDto(1, "Test", "test@gmail.com",
                "9123456789", UserDto.PreferredContact.SMS);
        messageBuilder = new LikeMessageBuilder(messageSource, userServiceClient);
        notificationServices = Collections.singletonList(notificationService);
        messageBuilders = Collections.singletonList(messageBuilder);
        likeEventListener = new LikeEventListener(notificationServices, messageBuilders,
                userServiceClient, objectMapper);
    }

    @Test
    public void testHandleLikeEventWithError() throws IOException {
        Message message = new DefaultMessage("testChannel".getBytes(), "Test".getBytes());
        when(objectMapper.readValue(message.getBody(), LikeEvent.class)).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class,
                () -> likeEventListener.handleEvent(message, LikeEvent.class));
    }

    @Test
    public void testHandleLikeEventSuccess() throws IOException {
        when(objectMapper.readValue(message.getBody(), LikeEvent.class)).thenReturn(likeEvent);

        likeEvent = likeEventListener.handleEvent(message, LikeEvent.class);

        verify(objectMapper).readValue(message.getBody(), LikeEvent.class);
        assertNotNull(likeEvent);
        assertEquals(1, likeEvent.postAuthorId());
        assertEquals(5, likeEvent.likeAuthorId());
        assertEquals(10, likeEvent.postId());
    }

    @Test
    public void testGetMessageWithNotExistingMessageBuilder() {
        messageBuilders = Collections.emptyList();
        likeEventListener = new LikeEventListener(notificationServices, messageBuilders,
                userServiceClient, objectMapper);

        assertThrows(IllegalArgumentException.class,
                () -> likeEventListener.getMessage(likeEvent, Locale.FRANCE));
    }

    @Test
    public void testSendNotificationWithNotExistNotificationService() {
        notificationServices = Collections.emptyList();
        likeEventListener = new LikeEventListener(notificationServices, messageBuilders,
                userServiceClient, objectMapper);
        when(userServiceClient.getUser(anyLong())).thenReturn(userDto);

        assertThrows(IllegalArgumentException.class,
                () -> likeEventListener.sendNotification(userDto.getId(), "message"));
    }
}
