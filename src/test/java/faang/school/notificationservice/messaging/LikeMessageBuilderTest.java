package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.LikeEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LikeMessageBuilderTest {
    @InjectMocks
    private LikeMessageBuilder likeMessageBuilder;

    @Test
    void buildMessage_ShouldReturnCorrectMessage() {
        LikeEvent event = LikeEvent.builder()
                .likeAuthorId(1L)
                .postId(2L)
                .build();
        Locale locale = Locale.ENGLISH;

        String message = likeMessageBuilder.buildMessage(event, locale);

        assertEquals("User with id: 1 liked your post with id: 2", message);
    }

    @Test
    void getInstance_ShouldReturnLikeEventClass() {
        Class<?> instanceClass = likeMessageBuilder.getInstance();

        assertEquals(LikeEvent.class, instanceClass);
    }
}