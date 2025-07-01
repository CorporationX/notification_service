package faang.school.notificationservice.messaging.comment;

import faang.school.notificationservice.config.messaging.CommentMessagesProperties;
import faang.school.notificationservice.model.kafka.comment.CommentMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @Mock
    private CommentMessagesProperties messageProperties;

    @InjectMocks
    private CommentMessageBuilder commentMessageBuilder;

    @Test
    void testBuildMessage_ReturnsCorrectFormattedMessage() {
        CommentMessage event = new CommentMessage();
        event.setPostTitle("Post Title");
        event.setUsernameAuthorComment("JohnDoe");
        event.setCommentContent("This is a comment");

        String expectedMessage = "New comment from JohnDoe on post Post Title: This is a comment";
        String labelKey = "comment.new";

        when(messageProperties.getNewLabel()).thenReturn(labelKey);
        when(messageSource.getMessage(eq(labelKey), any(), eq(Locale.ENGLISH)))
                .thenReturn(expectedMessage);

        String actual = commentMessageBuilder.buildMessage(event, Locale.ENGLISH);

        assertEquals(expectedMessage, actual);
        verify(messageSource).getMessage(eq(labelKey), any(), eq(Locale.ENGLISH));
    }

    @Test
    void testGetInstance_ReturnsCommentMessageClass() {
        assertEquals(CommentMessage.class, commentMessageBuilder.getInstance());
    }
}
