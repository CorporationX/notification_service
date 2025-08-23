package faang.school.notificationservice.builder;

import faang.school.notificationservice.event.RecommendationRequestedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RecommendationRequestMessageBuilderTest {

    private RecommendationRequestMessageBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new RecommendationRequestMessageBuilder();
    }

    @Test
    void buildMessage_shouldReturnCorrectText() {
        RecommendationRequestedEvent event = new RecommendationRequestedEvent(1L, 2L, 3L);

        String message = builder.buildMessage(event);

        assertEquals("Пользователь с ID 1 запросил рекомендацию.", message);
    }

    @Test
    void getInstance_shouldReturnCorrectClass() {
        assertEquals(RecommendationRequestedEvent.class, builder.getInstance());
    }
}