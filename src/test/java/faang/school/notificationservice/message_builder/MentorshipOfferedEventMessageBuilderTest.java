package faang.school.notificationservice.message_builder;

import faang.school.notificationservice.event.mentorship.MentorshipOfferedEvent;
import faang.school.notificationservice.messaging.message_builder.MentorshipOfferedEventMessageBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

@ExtendWith(MockitoExtension.class)
public class MentorshipOfferedEventMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    MentorshipOfferedEventMessageBuilder mentorshipOfferedEventMessageBuilder;

    @Test
    void testBuildMessage() {
        String toReturn = "return something";

        Mockito.when(messageSource
                .getMessage(Mockito.anyString(), Mockito.any(), Mockito.any(Locale.class))).thenReturn(toReturn);

        String message = mentorshipOfferedEventMessageBuilder
                .buildMessage(MentorshipOfferedEvent.builder().build(), Locale.getDefault());
        Assertions.assertEquals(toReturn, message);
    }
}