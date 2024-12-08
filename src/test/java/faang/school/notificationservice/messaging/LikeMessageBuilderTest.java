package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.LikeEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikeMessageBuilderTest {
    @InjectMocks
    private LikeMessageBuilder likeMessageBuilder;

    @Mock
    private MessageSource messageSource;

    @Test
    void buildMessageShouldReturnCorrectMessage() {
        LikeEvent event = LikeEvent.builder()
                .likeAuthorId(1L)
                .postId(2L)
                .build();
        Locale locale = Locale.ENGLISH;

        when(messageSource.getMessage(eq("like.add"), any(Object[].class), eq(locale)))
                .thenReturn("User with id: 1 liked your post with id: 2");

        String message = likeMessageBuilder.buildMessage(event, locale);

        assertEquals("User with id: 1 liked your post with id: 2", message);
    }

    @Test
    void getInstanceShouldReturnLikeEventClass() {
        Class<?> instanceClass = likeMessageBuilder.getInstance();

        assertEquals(LikeEvent.class, instanceClass);
    }
}