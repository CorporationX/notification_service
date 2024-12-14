package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.achievement.AchievementEvent;
import faang.school.notificationservice.dto.recommendation.RecommendationReceivedEvent;
import faang.school.notificationservice.messaging.achievement.AchievementMessageBuilder;
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
public class AchievementMessageBuilderTest {

    @Mock
    private AchievementMessageBuilder builder;

    @Test
    public void testGetInstance() {
        when(builder.getInstance()).thenReturn(AchievementEvent.class);

        Class<AchievementEvent> eventClass = builder.getInstance();

        assertEquals(AchievementEvent.class, eventClass);
    }

    @Test
    public void testBuildMessage() {
        AchievementEvent event = new AchievementEvent(
                "title",
                1L
        );
        when(builder.buildMessage(event, Locale.getDefault())).thenReturn("Some text");

        String text = builder.buildMessage(event, Locale.getDefault());

        assertEquals("Some text", text);
    }
}
