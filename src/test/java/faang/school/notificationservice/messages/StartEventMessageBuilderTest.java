package faang.school.notificationservice.messages;

import faang.school.notificationservice.dto.EventDto;
import faang.school.notificationservice.dto.GoalCompletedEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.Locale;

@ExtendWith(MockitoExtension.class)
public class StartEventMessageBuilderTest {
    @Spy
    private MessageSource messageSource;
    @InjectMocks
    private StartEventMessageBuilder startEventMessageBuilder;
    private final Locale locale = Locale.ENGLISH;
    private final String messageTemplate = "start_event_topic";
    private final String eventTitle = "Test";
    private EventDto eventDto;

    @BeforeEach
    public void setUp(){
        eventDto = new EventDto(1L, eventTitle, LocalDateTime.now());
    }

    @Test
    public void buildMessageCallsSourceMethodTest(){
        String expectedMessage = "The event Test has begun";
        System.out.println(messageSource.getMessage(messageTemplate, new Object[]{eventTitle}, locale));
        Mockito.when(messageSource.getMessage(messageTemplate, new Object[]{eventTitle}, locale))
                .thenReturn(String.format("The event %s has begun", eventTitle));

        Assertions.assertEquals(expectedMessage, startEventMessageBuilder.buildMessage(eventDto, locale));
    }
}
