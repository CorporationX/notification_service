package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.RecommendationReceivedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private RecommendationMessageBuilder recommendationMessageBuilder;

    private RecommendationReceivedEvent event;
    private Locale locale;

    @BeforeEach
    void setUp() {
        event = new RecommendationReceivedEvent(1L, 2L, "Receiver", 3L, "Author");
        locale = Locale.ENGLISH;
    }

    @Test
    @DisplayName("Build message with placeholders success")
    void testBuildMessageWithPlaceholdersSuccess() {
        Object[] placeholders = {"Receiver", "Author"};
        String code = "recommendation.new";
        when(messageSource.getMessage(code, placeholders, locale)).thenReturn("Congrats, Receiver! You've got a new recommendation from Author!");

        String result = recommendationMessageBuilder.buildMessage(event, locale);
        verify(messageSource, times(1)).getMessage(code, placeholders, locale);

        assertEquals("Congrats, Receiver! You've got a new recommendation from Author!", result);
    }

    @Test
    @DisplayName("getInstance return success")
    void testGetInstance_Success() {
        RecommendationMessageBuilder messageBuilder = new RecommendationMessageBuilder(null);

        Class<?> result = messageBuilder.getInstance();

        assertNotNull(result);
    }
}