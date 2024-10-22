package faang.school.notificationservice.builder;

import faang.school.notificationservice.redis.event.FollowerEvent;
import faang.school.notificationservice.messaging.FollowerEventBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class FollowerEventBuilderTest {
    @InjectMocks
    private FollowerEventBuilder followerEventBuilder;
    @Mock
    private MessageSource messageSource;

    @Test
    void testBuildMessage() {
        String checkMessage = "build message";
        FollowerEvent event = FollowerEvent.builder()
                .followedId(1L)
                .followerId(2L)
                .followerName("Mike")
                .build();

        when(messageSource.getMessage(anyString(), any(), any())).thenReturn(checkMessage);

        String result = followerEventBuilder.buildMessage(event, Locale.getDefault());

        assertEquals(checkMessage, result);
        verify(messageSource).getMessage("follower.new", new Object[]{event.followerName()}, Locale.getDefault());
    }

    @Test
    void testInstance() {
        var result = followerEventBuilder.getInstance();

        assertEquals(result, FollowerEvent.class);
    }
}
