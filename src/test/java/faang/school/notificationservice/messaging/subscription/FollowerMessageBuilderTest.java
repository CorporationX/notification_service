package faang.school.notificationservice.messaging.subscription;

import faang.school.notificationservice.dto.subscription.FollowerEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FollowerMessageBuilderTest {

    @Mock
    private FollowerMessageBuilder builder;

    @Test
    public void testGetInstance() {
        when(builder.getInstance()).thenReturn(FollowerEvent.class);

        Class<FollowerEvent> eventClass = builder.getInstance();

        assertEquals(FollowerEvent.class, eventClass);
    }

    @Test
    public void testBuildMessage() {
        FollowerEvent event = new FollowerEvent(
                1L,
                2L
        );
        when(builder.buildMessage(event, Locale.getDefault())).thenReturn("Some text");

        String text = builder.buildMessage(event, Locale.getDefault());

        assertEquals("Some text", text);
    }
}
