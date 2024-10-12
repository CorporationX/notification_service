package faang.school.notificationservice.messaging.comment;

import faang.school.notificationservice.dto.comment.CommentEventDto;
import faang.school.notificationservice.test_data.comment.TestDataCommentEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentMessageBuilderTest {
    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private CommentMessageBuilder commentMessageBuilder;

    private CommentEventDto event;

    @BeforeEach
    void setUp() {
        TestDataCommentEvent testDataCommentEvent = new TestDataCommentEvent();
        event = testDataCommentEvent.getCommentEventDto();
    }

    @Test
    void testGetInstance_Success() {
        Class<?> actualInstance = commentMessageBuilder.getInstance();

        assertEquals(CommentEventDto.class, actualInstance);
    }

    @Test
    void testBuildMessage_Success() {
        Object[] args = {
                event.getPostAuthorId(),
                event.getCommentAuthorId(),
                event.getPostId(),
                event.getCommentContent()
        };
        Locale locale = Locale.getDefault();
        String expectedMessage = "Hey User(userID: {0})! Someone(userID: {1}) left a new comment in your Post (postID: {2})! Comment: {3}";

        when(messageSource.getMessage("comment.new", args, locale)).thenReturn(expectedMessage);

        String actualMessage = commentMessageBuilder.buildMessage(event, locale);

        assertEquals(expectedMessage, actualMessage);
        verify(messageSource, atLeastOnce()).getMessage("comment.new", args, locale);
    }
}