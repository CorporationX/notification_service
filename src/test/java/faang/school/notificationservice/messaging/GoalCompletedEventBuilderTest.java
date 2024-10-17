package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.goal.GoalCompletedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoalCompletedEventBuilderTest {

    private static final String TEST = "TEST";

    @InjectMocks
    private GoalCompletedEventBuilder goalCompletedEventBuilder;

    @Mock
    private MessageSource messageSource;

    @Test
    @DisplayName("shouldReturnGoalCompletedEventClass")
    void whenCalledMethodThenReturnGoalCompletedEventClass() {
        assertEquals(GoalCompletedEvent.class, goalCompletedEventBuilder.getInstance());
    }

    @Test
    @DisplayName("shouldReturnExpectedMessage")
    void whenCalledMethodThenReturnExpectedMessage() {
        GoalCompletedEvent event = GoalCompletedEvent.builder()
                .goalName(TEST)
                .build();
        Locale locale = Locale.ENGLISH;

        when(messageSource.getMessage("goal.completed", new Object[]{TEST}, locale))
                .thenReturn(TEST);

        String message = goalCompletedEventBuilder.buildMessage(event, locale);

        assertEquals(TEST, message);
        verify(messageSource).getMessage("goal.completed", new Object[]{TEST}, locale);
    }
}