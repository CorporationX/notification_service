package faang.school.notificationservice.messageBuilder;


import faang.school.notificationservice.dto.like.LikeEvent;
import faang.school.notificationservice.messaging.LikeMessageBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LikeMessageBuilderTest {

    @Mock
    private LikeMessageBuilder builder;

    @Test
    public void testGetInstance() {
        when(builder.getInstance()).thenReturn(LikeEvent.class);

        Class<LikeEvent> eventClass = builder.getInstance();

        Assertions.assertEquals(LikeEvent.class, eventClass);
    }

    @Test
    public void testBuildMessage() {
        LikeEvent event = new LikeEvent(
                1L,
                2L,
                3L
        );
        when(builder.buildMessage(event, Locale.getDefault())).thenReturn("Some text");

        String text = builder.buildMessage(event, Locale.getDefault());

        Assertions.assertEquals("Some text", text);
    }
}
