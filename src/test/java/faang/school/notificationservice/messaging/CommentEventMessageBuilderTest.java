package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.event.CommentEventDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentEventMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private CommentEventMessageBuilder builder;

    private final CommentEventDto event = CommentEventDto.builder()
            .commentId(100L)
            .commenterId(200L)
            .postId(300L)
            .postAuthorId(400L)
            .text("Cool post!")
            .build();

    @Test
    void getInstance_ShouldReturnCommentEventDtoClass() {
        assertEquals(CommentEventDto.class, builder.getInstance());
    }

    @Test
    void buildMessage_ShouldBuildCorrectMessage() {
        Locale locale = Locale.ENGLISH;

        String expectedMessage = "User with ID 200 left a comment: \"Cool post!\" on your post ID 300";

        when(messageSource.getMessage(
                eq("notification.comment"),
                eq(new Object[]{"200", "Cool post!", "300"}),
                eq(locale)
        )).thenReturn(expectedMessage);

        String result = builder.buildMessage(event, locale);

        assertEquals(expectedMessage, result);
        verify(messageSource, times(1)).getMessage(eq("notification.comment"),
                eq(new Object[]{"200", "Cool post!", "300"}), eq(locale));
    }
}