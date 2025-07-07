package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.event.LikeEventDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikeEventMessageBuilderTest {
    private static final Long POST_ID = 1L;
    private static final Long COMMENT_ID = 2L;
    private static final Long AUTHOR_ID = 3L;
    private static final Long SENDER_ID = 4L;
    private static final String SENDER_NAME = "senderName";

    private final Locale locale = new Locale("ru");
    @Spy
    private MessageSource messageSource;
    @InjectMocks
    private LikeEventMessageBuilder likeEventMessageBuilder;

    @Test
    public void testGetInstance() {
        assertEquals(LikeEventDto.class, likeEventMessageBuilder.getInstance());
    }

    @Test
    public void testLikePostBuildMessage() {
        LikeEventDto event = getMockLikeEventDto(POST_ID, null);
        String expectedMessage = "message text";
        when(messageSource.getMessage(anyString(), any(), eq(locale))).thenReturn(expectedMessage);

        String actualMessage = likeEventMessageBuilder.buildMessage(event, locale);

        ArgumentCaptor<String> messageKey = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(messageKey.capture(), any(), eq(locale));
        String actualMessageKey = messageKey.getValue();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(LikeEventMessageBuilder.EVENT_LIKE_POST, actualMessageKey);
    }

    @Test
    public void testLikeCommentBuildMessage() {
        LikeEventDto event = getMockLikeEventDto(null, COMMENT_ID);
        String expectedMessage = "message text";
        when(messageSource.getMessage(anyString(), any(), eq(locale))).thenReturn(expectedMessage);

        String actualMessage = likeEventMessageBuilder.buildMessage(event, locale);

        ArgumentCaptor<String> messageKey = ArgumentCaptor.forClass(String.class);
        verify(messageSource).getMessage(messageKey.capture(), any(), eq(locale));
        String actualMessageKey = messageKey.getValue();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(LikeEventMessageBuilder.EVENT_LIKE_COMMENT, actualMessageKey);
    }

    @Test
    public void testFailBuildMessage() {
        LikeEventDto event = getMockLikeEventDto(null, null);

        String actualMessage = likeEventMessageBuilder.buildMessage(event, locale);

        verify(messageSource, times(0)).getMessage(anyString(), any(), eq(locale));
        assertEquals(0, actualMessage.length());
    }

    private LikeEventDto getMockLikeEventDto(Long postId, Long commentId) {
        return new LikeEventDto(AUTHOR_ID, SENDER_ID, SENDER_NAME, postId, commentId, LocalDateTime.now());
    }
}