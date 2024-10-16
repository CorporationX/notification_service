package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.PostServiceClient;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.comment.NewCommentEvent;
import faang.school.notificationservice.dto.post.PostDto;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.messaging.NewCommentEventBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.telegram.TelegramService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewCommentEventListenerTest {

    private static final long POST_ID = 1L;
    private static final long USER_ID = 1L;
    private static final long COMMENT_ID = 1L;

    private static final String POST_CONTENT = "CONTENT";
    private static final String COMMENT_CONTENT = "CONTENT";
    private static final String BUILDER_TEXT = "BUILDER_TEXT";

    @Mock
    private Message message;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private TelegramService telegramService;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private PostServiceClient postServiceClient;

    @Mock
    private NewCommentEventBuilder newCommentEventBuilder;

    private final Map<UserDto.PreferredContact, NotificationService> notificationServices = new HashMap<>();
    private final Map<Class<?>, MessageBuilder<?>> messageBuilders = new HashMap<>();
    private NewCommentEventListener newCommentEventListener;
    private NewCommentEvent newCommentEvent;
    private UserDto userDto;
    private PostDto postDto;

    @BeforeEach
    void init() {
        newCommentEventListener = new NewCommentEventListener(
                objectMapper,
                userServiceClient,
                postServiceClient,
                messageBuilders,
                notificationServices
        );

        newCommentEvent = NewCommentEvent.builder()
                .userId(USER_ID)
                .authorId(USER_ID)
                .postId(POST_ID)
                .content(COMMENT_CONTENT)
                .commentId(COMMENT_ID)
                .build();

        postDto = PostDto.builder()
                .id(POST_ID)
                .content(POST_CONTENT)
                .authorId(USER_ID)
                .build();

        userDto = UserDto.builder()
                .id(USER_ID)
                .notifyPreference(UserDto.PreferredContact.TELEGRAM)
                .build();
    }

    @Test
    @DisplayName("Should notify user about event")
    void whenCorrectValuesThenNotifyPerson() throws IOException {
        messageBuilders.put(newCommentEvent.getClass(), newCommentEventBuilder);
        notificationServices.put(UserDto.PreferredContact.TELEGRAM, telegramService);

        when(objectMapper.readValue(message.getBody(), NewCommentEvent.class))
                .thenReturn(newCommentEvent);
        when(postServiceClient.getPost(newCommentEvent.getPostId()))
                .thenReturn(postDto);
        when(userServiceClient.getUser(postDto.getAuthorId()))
                .thenReturn(userDto);
        when(newCommentEventBuilder.buildMessage(eq(newCommentEvent), any(Locale.class)))
                .thenReturn(BUILDER_TEXT);

        doNothing().when(telegramService).send(eq(userDto), anyString());

        newCommentEventListener.onMessage(message, new byte[0]);

        verify(objectMapper)
                .readValue(message.getBody(), NewCommentEvent.class);
        verify(postServiceClient)
                .getPost(newCommentEvent.getPostId());
        verify(userServiceClient)
                .getUser(postDto.getAuthorId());
        verify(telegramService)
                .send(eq(userDto), anyString());
    }

    @Test
    @DisplayName("Should throw exception when not found any correct message builder")
    void whenNoMessageBuildersThenThrowException() throws IOException {
        when(objectMapper.readValue(message.getBody(), NewCommentEvent.class))
                .thenReturn(newCommentEvent);
        when(postServiceClient.getPost(newCommentEvent.getPostId()))
                .thenReturn(postDto);
        when(userServiceClient.getUser(postDto.getAuthorId()))
                .thenReturn(userDto);

        assertThrows(NoSuchElementException.class,
                () -> newCommentEventListener.onMessage(message, new byte[0]),
                "Not found message builder");
    }

    @Test
    @DisplayName("Should throw exception when not found any correct preferred notification method")
    void whenNoPreferredNotificationMethodThenThrowException() throws IOException {
        messageBuilders.put(newCommentEvent.getClass(), newCommentEventBuilder);

        when(objectMapper.readValue(message.getBody(), NewCommentEvent.class))
                .thenReturn(newCommentEvent);
        when(postServiceClient.getPost(newCommentEvent.getPostId()))
                .thenReturn(postDto);
        when(userServiceClient.getUser(postDto.getAuthorId()))
                .thenReturn(userDto);
        when(newCommentEventBuilder.buildMessage(eq(newCommentEvent), any(Locale.class)))
                .thenReturn(BUILDER_TEXT);

        assertThrows(NoSuchElementException.class,
                () -> newCommentEventListener.onMessage(message, new byte[0]),
                "Not found notification service");
    }
}