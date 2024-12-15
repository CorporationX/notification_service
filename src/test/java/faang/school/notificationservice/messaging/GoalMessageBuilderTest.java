package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.GoalCompletedEvent;
import faang.school.notificationservice.event.LikeEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GoalMessageBuilderTest {
    private static final String EXPECTED_MESSAGE = "Goal with id: 1 was completed by user with id: 1";

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private GoalMessageBuilder goalMessageBuilder;

    @Test
    @DisplayName("buildMessage should return correct message")
    void buildMessageShouldReturnCorrectMessage() {
        GoalCompletedEvent event = new GoalCompletedEvent(1L, 1L);
        Locale locale = Locale.ENGLISH;

        when(messageSource.getMessage(eq("goal.completed"), any(Object[].class), eq(locale)))
                .thenReturn(EXPECTED_MESSAGE);

        String message = goalMessageBuilder.buildMessage(event, locale);

        assertEquals(EXPECTED_MESSAGE, message);
    }

    @Test
    @DisplayName("getInstance should return GoalEvent class")
    void getInstanceShouldReturnGoalEventClass() {
        Class<?> instanceClass = goalMessageBuilder.getInstance();

        assertEquals(GoalCompletedEvent.class, instanceClass);
    }
}
