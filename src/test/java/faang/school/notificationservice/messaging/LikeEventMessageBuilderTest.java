package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.event.LikeEvent;
import faang.school.notificationservice.properties.EventType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikeEventMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private LikeEventMessageBuilder builder;

    private final LikeEvent event = LikeEvent.builder()
            .postId(300L)
            .authorId(400L)
            .userId(200L)
            .likedAt(LocalDateTime.now())
            .type(EventType.LIKED_POST)
            .build();

    @Test
    void getInstance_ShouldReturnLikeEventClass() {
        assertEquals(LikeEvent.class, builder.getInstance());
    }

    @Test
    void buildMessage_ShouldBuildCorrectMessage() {
        Locale locale = Locale.ENGLISH;

        String expectedMessage = "User with ID 200 liked your post with ID 300";

        when(messageSource.getMessage(
                eq("notification.like"),
                eq(new Object[]{"200", "300"}),
                eq(locale)
        )).thenReturn(expectedMessage);

        String result = builder.buildMessage(event, locale);

        assertEquals(expectedMessage, result);
        verify(messageSource, times(1)).getMessage(eq("notification.like"),
                eq(new Object[]{"200", "300"}), eq(locale));
    }
}
