package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.CommentEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentMessageBuilderTest {
    @Mock
    private MessageSource messageSource;

    private CommentMessageBuilder commentMessageBuilder;

    @BeforeEach
    void setUp() {
        commentMessageBuilder = new CommentMessageBuilder(messageSource);
    }

    @Test
    void buildMessageShouldReturnFormattedMessageWhenEventIsValid() {
        CommentEvent event = CommentEvent.builder().commentAuthorId(1L).postId(2L).commentId(3L).postAuthorId(4L).build();
        Locale locale = Locale.ENGLISH;
        String expectedMessage = "User 1 commented on post 2";

        when(messageSource.getMessage("comment.add", new Object[]{1L, 2L}, locale)).thenReturn(expectedMessage);

        String result = commentMessageBuilder.buildMessage(event, locale);

        assertEquals(expectedMessage, result);
        verify(messageSource, times(1)).getMessage("comment.add", new Object[]{1L, 2L}, locale);
    }

    @Test
    void getInstanceShouldReturnCommentEventClass() {
        Class<CommentEvent> eventClass = commentMessageBuilder.getInstance();

        assertEquals(CommentEvent.class, eventClass);
    }
}