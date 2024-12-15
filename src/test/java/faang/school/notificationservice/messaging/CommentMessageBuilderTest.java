package faang.school.notificationservice.messaging;

import faang.school.notificationservice.data.NotificationChannel;
import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.event.CommentEvent;
import faang.school.notificationservice.listener.CommentEventListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

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

    @Mock
    private CommentEventListener commentEventListener;

    @BeforeEach
    void setUp() {
        commentMessageBuilder = new CommentMessageBuilder(messageSource);
    }

    @Test
    void buildMessageShouldReturnFormattedMessageWhenEventIsValid() {
        CommentEvent event = CommentEvent.builder()
                .commentAuthorId(2L)
                .postId(2L)
                .commentId(3L)
                .postAuthorId(1L)
                .postAuthorName("test")
                .build();

        Locale locale = LocaleContextHolder.getLocale();
        String expectedMessage = "Congrats, test! You''ve added a new comment to your post with id: 2";

        when(messageSource.getMessage("comment.add", new Object[]{"test", 2L}, locale)).thenReturn(expectedMessage);

        String result = commentMessageBuilder.buildMessage(event, locale);

        assertEquals(expectedMessage, result);
        verify(messageSource, times(1)).getMessage("comment.add", new Object[]{"test", 2L}, locale);
    }

    @Test
    void getInstanceShouldReturnCommentEventClass() {
        Class<CommentEvent> eventClass = commentMessageBuilder.getInstance();

        assertEquals(CommentEvent.class, eventClass);
    }
}