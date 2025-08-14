package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.CommentEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentEventMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    private CommentEventMessageBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new CommentEventMessageBuilder(messageSource);
    }

    @Test
    @DisplayName("Should return CommentEvent class from getInstance")
    void shouldReturnCommentEventClass() {
        assertEquals(CommentEvent.class, builder.getInstance());
    }

    @Test
    @DisplayName("Should build message using correct key, args, and locale")
    public void shouldBuildMessageWithCorrectParameters() {
        CommentEvent e = new CommentEvent(1L, 2L, 3L, 4L, "text");
        Locale locale = Locale.ENGLISH;
        String expected = "rendered";
        Object[] expectedArgs = {e.postId(), e.authorId(), e.content()};

        when(messageSource.getMessage("comment.received", expectedArgs, locale))
                .thenReturn(expected);

        String actual = builder.buildMessage(e, locale);

        assertEquals(expected, actual);
        verify(messageSource).getMessage("comment.received", expectedArgs, locale);
        verifyNoMoreInteractions(messageSource);
    }
}
