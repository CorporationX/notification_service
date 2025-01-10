package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.event.GoalCompletedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class GoalCompletedMessageBuilderTest {
    @InjectMocks
    private GoalCompletedMessageBuilder goalCompletedMessageBuilder;

    @Mock
    private MessageSource messageSource;

    private String message;
    private GoalCompletedEvent event;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        event = new GoalCompletedEvent();
        event.setUserId(1L);
        event.setGoalId(4L);

        message = "message";
    }

    @Test
    void testGetInstance(){
        Class<?> clas = goalCompletedMessageBuilder.getInstance();
        assertEquals(clas, GoalCompletedEvent.class);
    }

    @Test
    void testBuildMessage(){
        when(messageSource.getMessage("goal.completed", new Object[]{event.getGoalId()}, Locale.UK)).thenReturn(message);

        String resultMessage = goalCompletedMessageBuilder.buildMessage(event, Locale.UK);

        assertEquals(resultMessage,message);
    }
}
