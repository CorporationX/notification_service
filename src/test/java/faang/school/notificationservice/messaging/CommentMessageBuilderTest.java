package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.CommentEvent;
import faang.school.notificationservice.dto.NotificationData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import java.util.Locale;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommentMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private CommentMessageBuilder commentMessageBuilder;

    private CommentEvent commentEvent;
    private NotificationData notificationData;

    @BeforeEach
    void setUp() {
        commentEvent = CommentEvent.builder()
                .build();
        notificationData = NotificationData.builder()
                .follower("Max")
                .build();
    }

    @Test
    public void whenGetInstanceThenGetProjectFollowerEventClass() {
        assertThat(commentMessageBuilder.getInstance()).isEqualTo(commentEvent.getClass());
    }

    @Test
    public void whenBuildMessageThenReturnMessage() {
        String message = "You've got a new comment from Max";
        when(messageSource.getMessage(any(), any(), any())).thenReturn(message);
        String actual = commentMessageBuilder.buildMessage(commentEvent, Locale.UK, notificationData);
        assertThat(actual).isEqualTo(message);
    }
}