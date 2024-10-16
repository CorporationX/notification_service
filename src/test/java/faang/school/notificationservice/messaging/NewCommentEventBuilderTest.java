package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.comment.NewCommentEvent;
import org.junit.jupiter.api.DisplayName;
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
class NewCommentEventBuilderTest {

    private static final Class<?> EXPECTED_INSTANCE = NewCommentEvent.class;

    private static final String TEST = "TEST";
    private static final String KEY_FOR_MESSAGE = "comment.new";

    @InjectMocks
    private NewCommentEventBuilder newCommentEventBuilder;

    @Mock
    private MessageSource messageSource;

    @Test
    @DisplayName("Should return expected class instance")
    void whenMethodCalledThenReturnExpectedInstance() {
        assertEquals(EXPECTED_INSTANCE, newCommentEventBuilder.getInstance());
    }

    @Test
    @DisplayName("Should return full short message")
    void whenMethodCalledThenReturnFullShortMessage() {
        NewCommentEvent newCommentEvent = NewCommentEvent.builder()
                .content(TEST)
                .build();
        Locale locale = Locale.ENGLISH;

        when(messageSource.getMessage(eq(KEY_FOR_MESSAGE), any(), eq(locale)))
                .thenReturn(TEST);

        String message = newCommentEventBuilder.buildMessage(newCommentEvent, locale);

        assertEquals(TEST, message);
        verify(messageSource).getMessage(eq(KEY_FOR_MESSAGE), any(), eq(locale));
    }
}