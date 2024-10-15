package faang.school.notificationservice.builder;

import faang.school.notificationservice.messaging.MentorshipOfferedMessageBuilder;
import faang.school.notificationservice.model.event.MentorshipOfferedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MentorshipOfferedMessageBuilderTest {
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private MentorshipOfferedMessageBuilder mentorshipOfferedMessageBuilder;

    @Test
    void testBuilderOk() {
        MentorshipOfferedEvent mentorshipOfferedEvent = MentorshipOfferedEvent.builder()
                .requestId(1L)
                .requesterId(1L)
                .receiverId(2L)
                .build();

        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("dedushka");

        assertEquals("dedushka", mentorshipOfferedMessageBuilder.buildMessage(mentorshipOfferedEvent, Locale.getDefault()));
    }
}
