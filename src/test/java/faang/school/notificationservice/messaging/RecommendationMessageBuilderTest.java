package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.recommendation.RecommendationReceivedEvent;
import faang.school.notificationservice.messaging.recommendation.RecommendationMessageBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecommendationMessageBuilderTest {

    @Mock
    private RecommendationMessageBuilder builder;

    @Test
    public void testGetInstance() {
        when(builder.getInstance()).thenReturn(RecommendationReceivedEvent.class);

        Class<RecommendationReceivedEvent> eventClass = builder.getInstance();

        assertEquals(RecommendationReceivedEvent.class, eventClass);
    }

    @Test
    public void testBuildMessage() {
        RecommendationReceivedEvent event = new RecommendationReceivedEvent(
                1L,
                2L,
                3L,
                "Content",
                LocalDateTime.now()
        );
        when(builder.buildMessage(event, Locale.getDefault())).thenReturn("Some text");

        String text = builder.buildMessage(event, Locale.getDefault());

        assertEquals("Some text", text);
    }
}
