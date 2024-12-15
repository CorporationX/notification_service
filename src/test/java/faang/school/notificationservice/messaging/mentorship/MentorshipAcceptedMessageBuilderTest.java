package faang.school.notificationservice.messaging.mentorship;

import faang.school.notificationservice.dto.mentorship.MentorshipAcceptedEvent;
import faang.school.notificationservice.messaging.mentroship.MentorshipAcceptedMessageBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MentorshipAcceptedMessageBuilderTest {

    @Mock
    private MentorshipAcceptedMessageBuilder builder;

    @Test
    void testGetInstance() {
        when(builder.getInstance()).thenReturn(MentorshipAcceptedEvent.class);

        Class<MentorshipAcceptedEvent> eventClass = builder.getInstance();

        assertEquals(MentorshipAcceptedEvent.class, eventClass);
    }

    @Test
    void testBuildMessage() {
        MentorshipAcceptedEvent event = new MentorshipAcceptedEvent(
                1L,
                2L,
                3L,
                LocalDateTime.now());
        when(builder.buildMessage(event, Locale.getDefault())).thenReturn("Some text");

        String text = builder.buildMessage(event, Locale.getDefault());

        assertEquals("Some text", text);
    }

}
