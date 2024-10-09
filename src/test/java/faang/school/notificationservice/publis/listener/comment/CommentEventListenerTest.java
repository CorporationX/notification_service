package faang.school.notificationservice.publis.listener.comment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.comment.CommentEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.test_data.comment.TestDataCommentEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.util.Collections;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentEventListenerTest {
    @Mock
    private Message message;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private MessageBuilder<CommentEventDto> messageBuilder;
    @Mock
    private NotificationService notificationService;
    @InjectMocks
    private CommentEventListener commentEventListener;

    private UserDto postAuthor;
    private CommentEventDto commentEventDto;

    @BeforeEach
    void setUp() {
        commentEventListener = new CommentEventListener(
                objectMapper,
                userServiceClient,
                Collections.singletonList(messageBuilder),
                Collections.singletonList(notificationService));

        TestDataCommentEvent testDataCommentEvent = new TestDataCommentEvent();

        postAuthor = testDataCommentEvent.getPostAuthor();
        commentEventDto = testDataCommentEvent.getCommentEventDto();
    }

    @Nested
    class PositiveTests {
        @Test
        void testOnMessage_Success() throws JsonProcessingException {
            String messageBody = commentEventDto.toString();
            byte[] pattern = {};
            String notificationContent = "Test comment notification";

            when(message.getBody()).thenReturn(messageBody.getBytes());
            when(objectMapper.readValue(messageBody, CommentEventDto.class)).thenReturn(commentEventDto);
            when(messageBuilder.getInstance()).thenReturn((Class) CommentEventDto.class);
            when(messageBuilder.buildMessage(commentEventDto, Locale.getDefault())).thenReturn(notificationContent);
            when(userServiceClient.getUser(postAuthor.getId())).thenReturn(postAuthor);
            when(notificationService.getPreferredContact()).thenReturn(postAuthor.getPreference());

            commentEventListener.onMessage(message, pattern);

            verify(objectMapper, atLeastOnce()).readValue(messageBody, CommentEventDto.class);
        }
    }

    @Nested
    class NegativeTests {
        @Test
        void testGetMessage_NoMatchingBuilder_throwRuntimeException() throws JsonProcessingException {
            String messageBody = "invalid_json";
            byte[] pattern = {};

            when(message.getBody()).thenReturn(messageBody.getBytes());
            when(objectMapper.readValue(messageBody, CommentEventDto.class)).thenThrow(
                    new JsonProcessingException("Error parsing JSON") {}
            );

            RuntimeException thrown = assertThrows(RuntimeException.class,
                    () -> commentEventListener.onMessage(message, pattern)
            );

            assertEquals("Error parsing JSON", thrown.getCause().getMessage());
        }
    }
}
