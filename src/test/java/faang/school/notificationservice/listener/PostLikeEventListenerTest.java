package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.PostLikeEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.listener.PostLikeEventListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static faang.school.notificationservice.service.listener.AbstractEventListener.FAILED_PARSE_OBJECT;
import static faang.school.notificationservice.service.listener.AbstractEventListener.MESSAGE_BUILDER_NOT_FOUND;
import static faang.school.notificationservice.service.listener.AbstractEventListener.NOTIFICATION_SERVICE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

@ExtendWith(MockitoExtension.class)
public class PostLikeEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MessageBuilder<PostLikeEvent> messageBuilder;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private PostLikeEventListener postLikeEventListener;

    private PostLikeEvent testEvent;
    private UserDto testUser;
    private final String testMessage = "test message";
    private final String jsonMessage = "{\"postAuthorId\":1, \"likeAuthorId\":1, \"postId\":1}";

    @BeforeEach
    void setUp() {
        List<NotificationService> notificationServices = List.of(notificationService);
        List<MessageBuilder<PostLikeEvent>> messageBuilders = List.of(messageBuilder);

        ReflectionTestUtils.setField(postLikeEventListener, "notificationServices", notificationServices);
        ReflectionTestUtils.setField(postLikeEventListener, "messageBuilders", messageBuilders);

        testEvent = new PostLikeEvent(1L, 1L, 1L);
        testUser = new UserDto(1L, "Sasha", "@mail", "+7900", UserDto.PreferredContact.EMAIL);
    }

    @Test
    void shouldHandleValidMessage() throws JsonProcessingException {
        when(objectMapper.readValue(jsonMessage, PostLikeEvent.class)).thenReturn(testEvent);
        when(userServiceClient.getUser(testEvent.getPostAuthorId())).thenReturn(testUser);
        when(messageBuilder.getInstance()).thenReturn(PostLikeEvent.class);
        when(messageBuilder.buildMessage(testEvent, Locale.UK)).thenReturn(testMessage);
        when(notificationService.getPreferredContact()).thenReturn(testUser.getPreference());

        postLikeEventListener.listen(jsonMessage);

        verify(objectMapper).readValue(jsonMessage, PostLikeEvent.class);
        verify(userServiceClient).getUser(testEvent.getPostAuthorId());
        verify(notificationService).send(testUser, testMessage);
    }

    @Test
    void shouldHandleJsonProcessingException() throws JsonProcessingException {
        when(objectMapper.readValue(jsonMessage, PostLikeEvent.class))
                .thenThrow(new JsonProcessingException("Error") {});

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> postLikeEventListener.listen(jsonMessage));

        assertEquals(FAILED_PARSE_OBJECT, exception.getMessage());
    }

    @Test
    void shouldHandleNoMessageBuilder() throws JsonProcessingException {
        ReflectionTestUtils.setField(postLikeEventListener, "messageBuilders", Collections.emptyList());
        when(objectMapper.readValue(jsonMessage, PostLikeEvent.class)).thenReturn(testEvent);
        when(userServiceClient.getUser(testEvent.getPostAuthorId())).thenReturn(testUser);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> postLikeEventListener.listen(jsonMessage));

        assertEquals(MESSAGE_BUILDER_NOT_FOUND + testEvent.getClass().getName(), exception.getMessage());
    }

    @Test
    void shouldHandleNoNotificationService() throws JsonProcessingException {
        ReflectionTestUtils.setField(postLikeEventListener, "notificationServices", Collections.emptyList());
        when(objectMapper.readValue(jsonMessage, PostLikeEvent.class)).thenReturn(testEvent);
        when(userServiceClient.getUser(testEvent.getPostAuthorId())).thenReturn(testUser);
        when(messageBuilder.getInstance()).thenReturn(PostLikeEvent.class);
        when(messageBuilder.buildMessage(testEvent, Locale.UK)).thenReturn(testMessage);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> postLikeEventListener.listen(jsonMessage));

        assertEquals(NOTIFICATION_SERVICE_NOT_FOUND, exception.getMessage());
    }
}
