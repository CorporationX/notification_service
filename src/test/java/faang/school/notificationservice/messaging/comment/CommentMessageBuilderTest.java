package faang.school.notificationservice.messaging.comment;

import faang.school.notificationservice.dto.event.CommentEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private CommentMessageBuilder commentMessageBuilder;

    private static final String MESSAGE_KEY = "comment.new";
    private static final long POST_ID = 123L;
    private static final String EXPECTED_MESSAGE = "A new comment was added to your post #123: Test comment";

    @Test
    void testGetInstance() {
        Class<?> instance = commentMessageBuilder.getInstance();
        assertThat(instance).isEqualTo(CommentEvent.class);
    }

    @Test
    void testBuildMessage_WithNormalComment() {
        CommentEvent event = CommentEvent.builder()
                .commentId(1L)
                .postId(POST_ID)
                .commentText("Test comment")
                .build();

        when(messageSource.getMessage(
                eq(MESSAGE_KEY),
                any(Object[].class),
                any(Locale.class)
        )).thenReturn(EXPECTED_MESSAGE);

        String result = commentMessageBuilder.buildMessage(event, Locale.ENGLISH);

        assertThat(result).isEqualTo(EXPECTED_MESSAGE);
    }

    @Test
    void testBuildMessage_WithNullCommentText() {
        CommentEvent event = CommentEvent.builder()
                .commentId(1L)
                .postId(POST_ID)
                .commentText(null)
                .build();

        when(messageSource.getMessage(
                eq(MESSAGE_KEY),
                eq(new Object[]{POST_ID, ""}),
                any(Locale.class)
        )).thenReturn("A new comment was added to your post #123: ");

        String result = commentMessageBuilder.buildMessage(event, Locale.ENGLISH);

        assertThat(result).isEqualTo("A new comment was added to your post #123: ");
    }

    @Test
    void testBuildMessage_WithEmptyCommentText() {
        CommentEvent event = CommentEvent.builder()
                .commentId(1L)
                .postId(POST_ID)
                .commentText("")
                .build();

        when(messageSource.getMessage(
                eq(MESSAGE_KEY),
                eq(new Object[]{POST_ID, ""}),
                any(Locale.class)
        )).thenReturn("A new comment was added to your post #123: ");

        String result = commentMessageBuilder.buildMessage(event, Locale.ENGLISH);

        assertThat(result).isEqualTo("A new comment was added to your post #123: ");
    }

    @Test
    void testBuildMessage_WithLongCommentText_ShouldTruncate() {
        String longText = "A".repeat(150);
        CommentEvent event = CommentEvent.builder()
                .commentId(1L)
                .postId(POST_ID)
                .commentText(longText)
                .build();

        String expectedTruncated = "A".repeat(100) + "...";
        when(messageSource.getMessage(
                eq(MESSAGE_KEY),
                eq(new Object[]{POST_ID, expectedTruncated}),
                any(Locale.class)
        )).thenReturn("A new comment was added to your post #123: " + expectedTruncated);

        String result = commentMessageBuilder.buildMessage(event, Locale.ENGLISH);

        assertThat(result).contains(expectedTruncated);
    }

    @Test
    void testBuildMessage_WithExactMaxLengthCommentText_ShouldNotTruncate() {
        String exactLengthText = "A".repeat(100);
        CommentEvent event = CommentEvent.builder()
                .commentId(1L)
                .postId(POST_ID)
                .commentText(exactLengthText)
                .build();

        when(messageSource.getMessage(
                eq(MESSAGE_KEY),
                eq(new Object[]{POST_ID, exactLengthText}),
                any(Locale.class)
        )).thenReturn("A new comment was added to your post #123: " + exactLengthText);

        String result = commentMessageBuilder.buildMessage(event, Locale.ENGLISH);

        assertThat(result).contains(exactLengthText);
        assertThat(result).doesNotContain("...");
    }

    @ParameterizedTest
    @MethodSource("localeProvider")
    void testBuildMessage_WithDifferentLocales(Locale locale) {
        CommentEvent event = CommentEvent.builder()
                .commentId(1L)
                .postId(POST_ID)
                .commentText("Test comment")
                .build();

        when(messageSource.getMessage(
                eq(MESSAGE_KEY),
                any(Object[].class),
                eq(locale)
        )).thenReturn("Localized message for " + locale);

        String result = commentMessageBuilder.buildMessage(event, locale);

        assertThat(result).isEqualTo("Localized message for " + locale);
    }

    private static Stream<Arguments> localeProvider() {
        return Stream.of(
                Arguments.of(Locale.ENGLISH),
                Arguments.of(Locale.forLanguageTag("ru")),
                Arguments.of(Locale.forLanguageTag("de")),
                Arguments.of(Locale.getDefault())
        );
    }
}

