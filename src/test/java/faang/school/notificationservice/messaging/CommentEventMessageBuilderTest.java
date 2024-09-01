package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.events.CommentEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CommentEventMessageBuilderTest {

    @InjectMocks
    private CommentEventMessageBuilder commentEventMessageBuilder;

    @Mock
    private MessageSource messageSource;

    @Mock
    private UserServiceClient userServiceClient;

    @Test
    void testGetInstance() {
        Class<?> result = commentEventMessageBuilder.getInstance();
        assertEquals(CommentEventMessageBuilder.class, result);
    }

    @BeforeEach
    void setUp() {
        System.setProperty("DB_USERNAME", "test");
        System.setProperty("DB_PASSWORD", "test");
    }

    @Test
    void testBuildMessage() {
        CommentEvent event = CommentEvent.builder()
                .authorId(1L)
                .commentContent("content")
                .build();
        UserDto commentAuthor = UserDto.builder()
                .username("Username")
                .build();
        Object[] args = {commentAuthor.getUsername(), event.getCommentContent()};

        Mockito.when(userServiceClient.getUser(1L)).thenReturn(commentAuthor);

        commentEventMessageBuilder.buildMessage(event, Locale.CHINA);

        Mockito.verify(userServiceClient, Mockito.times(1)).getUser(1L);
        Mockito.verify(messageSource, Mockito.times(1)).getMessage("comment.created", args, Locale.CHINA);

    }

    @Test
    void testSupportsEvent() {
        Class<?> result = commentEventMessageBuilder.supportsEvent();
        assertEquals(CommentEvent.class, result);
    }

}