package faang.school.notificationservice.builder.message;

import faang.school.notificationservice.builder.message.impl.PostLikeMessageBuilder;
import faang.school.notificationservice.config.messageSource.MessageKeys;
import faang.school.notificationservice.message.event.PostLikeEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostLikeMessageBuilderTest {

    @Mock
    private MessageKeys messageKeys;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private PostLikeMessageBuilder messageBuilder;

    @Test
    void testBuild_ShouldReturnCorrectMessage() {
        PostLikeEvent event = new PostLikeEvent();
        event.setAuthorName("John Doe");
        Locale locale = Locale.ENGLISH;
        String messageKey = "like.post.message";
        String expectedMessage = "John Doe liked your post";

        when(messageKeys.getLikePost()).thenReturn(messageKey);
        when(messageSource.getMessage(
                messageKey,
                new Object[]{event.getAuthorName()},
                locale
        )).thenReturn(expectedMessage);

        String actualMessage = messageBuilder.build(event, locale);

        assertEquals(expectedMessage, actualMessage);
        verify(messageKeys).getLikePost();
        verify(messageSource).getMessage(messageKey, new Object[]{event.getAuthorName()}, locale);
    }

    @Test
    void testBuild_WithDifferentLocale_ShouldReturnLocalizedMessage() {
        PostLikeEvent event = new PostLikeEvent();
        event.setAuthorName("John Doe");
        Locale locale = Locale.FRENCH;
        String messageKey = "like.post.message";
        String expectedMessage = "John Doe a aimé votre publication";

        when(messageKeys.getLikePost()).thenReturn(messageKey);
        when(messageSource.getMessage(
                messageKey,
                new Object[]{event.getAuthorName()},
                locale
        )).thenReturn(expectedMessage);

        String actualMessage = messageBuilder.build(event, locale);

        assertEquals(expectedMessage, actualMessage);
        verify(messageKeys).getLikePost();
        verify(messageSource).getMessage(messageKey, new Object[]{event.getAuthorName()}, locale);
    }

    @Test
    void build_WhenMessageSourceThrowsException_ShouldPropagateException() {
        PostLikeEvent event = new PostLikeEvent();
        event.setAuthorName("John Doe");
        Locale locale = Locale.ENGLISH;
        String messageKey = "like.post.message";

        when(messageKeys.getLikePost()).thenReturn(messageKey);
        when(messageSource.getMessage(
                messageKey,
                new Object[]{event.getAuthorName()},
                locale
        )).thenThrow(new NoSuchMessageException("Message not found"));

        assertThrows(NoSuchMessageException.class, () -> messageBuilder.build(event, locale));
    }
}
