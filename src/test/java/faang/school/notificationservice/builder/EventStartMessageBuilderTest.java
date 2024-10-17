package faang.school.notificationservice.builder;

import faang.school.notificationservice.model.event.EventStartEvent;
import faang.school.notificationservice.messaging.EventStartMessageBuilder;
import faang.school.notificationservice.messaging.MentorshipAcceptedEventMessageBuilder;
import faang.school.notificationservice.model.event.MentorshipAcceptedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class EventStartMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private MentorshipAcceptedEventMessageBuilder builder;

    @Test
    void testBuilderOk() {
        MentorshipAcceptedEvent event = MentorshipAcceptedEvent.builder().build();

        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("hello there");

        assertEquals("hello there", builder.buildMessage(event, Locale.getDefault()));
    }
}
