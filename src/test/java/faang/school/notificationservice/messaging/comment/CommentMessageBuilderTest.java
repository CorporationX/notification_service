package faang.school.notificationservice.messaging.comment;

import faang.school.notificationservice.model.kafka.comment.CommentMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentMessageBuilderTest {
    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private CommentMessageBuilder messageBuilder;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(messageBuilder, "code", "comment.new");
    }

    @Test
    void testBuildMessage_returnsExpectedMessage() {
        CommentMessage model = new CommentMessage();
        model.setPostTitle("Spring Testing");
        model.setUsernameAuthorComment("john_doe");
        model.setCommentContent("Great post!");
        Object[] expectedArgs = {
                model.getPostTitle(),
                model.getUsernameAuthorComment(),
                model.getCommentContent()
        };
        Locale locale = Locale.FRANCE;
        String expectedMessage = "Vous avez un nouveau commentaire sur votre publication Spring Testing de john_doe : Great post!";

        when(messageSource.getMessage("comment.new", expectedArgs, locale)).thenReturn(expectedMessage);

        String result = messageBuilder.buildMessage(model, locale);

        assertEquals(expectedMessage, result);
        verify(messageSource).getMessage("comment.new", expectedArgs, locale);
    }
}