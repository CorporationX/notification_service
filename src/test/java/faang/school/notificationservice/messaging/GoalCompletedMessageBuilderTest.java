package faang.school.notificationservice.messaging;

import faang.school.notificationservice.model.GoalCompletedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GoalCompletedMessageBuilderTest {

    private MessageSource messageSource;
    private GoalCompletedMessageBuilder builder;

    @BeforeEach
    void setUp() {
        messageSource = Mockito.mock(MessageSource.class);
        builder = new GoalCompletedMessageBuilder(messageSource);
    }

    @Test
    void shouldBuildCorrectMessage() {
        GoalCompletedEvent event = new GoalCompletedEvent();
        event.setGoalId(123L);
        Locale locale = new Locale("ru");
        String expectedMessage = "Поздравляем! Вы достигли цели №123!";

        Mockito.when(messageSource.getMessage(
                Mockito.eq("goal.completed"),
                Mockito.eq(new Object[]{123L}),
                Mockito.eq(locale)
        )).thenReturn(expectedMessage);

        String actualMessage = builder.buildMessage(event, locale);

        assertEquals(expectedMessage, actualMessage);
    }
}