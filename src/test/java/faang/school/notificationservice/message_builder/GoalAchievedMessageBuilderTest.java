package faang.school.notificationservice.message_builder;

import faang.school.notificationservice.dto.GoalCompletedEvent;
import faang.school.notificationservice.messages.GoalAchievedMessageBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
public class GoalAchievedMessageBuilderTest {
    @Spy
    private MessageSource messageSource;
    @InjectMocks
    private GoalAchievedMessageBuilder goalAchievedMessageBuilder;
    private final String PROPERTIES_FILE_NAME = "goal.completed";
    private final Long completedGoalId = 10L;
    private GoalCompletedEvent goalCompletedEvent;
    private final Locale locale = Locale.ENGLISH;

    @BeforeEach
    public void setUp(){
        goalCompletedEvent = new GoalCompletedEvent(completedGoalId);
    }

    @Test
    public void buildMessageCallsSourceMethodTest(){
        String expectedMessage = "Goal 10 completed!";
        Mockito.when(messageSource.getMessage(PROPERTIES_FILE_NAME, new Object[]{completedGoalId}, locale))
                .thenReturn(String.format("Goal %d completed!", completedGoalId));

        assertEquals(expectedMessage, goalAchievedMessageBuilder.getMessage(goalCompletedEvent, locale));
        Mockito.verify(messageSource, Mockito.times(1))
                .getMessage(PROPERTIES_FILE_NAME, new Object[]{completedGoalId}, locale);
    }
}
