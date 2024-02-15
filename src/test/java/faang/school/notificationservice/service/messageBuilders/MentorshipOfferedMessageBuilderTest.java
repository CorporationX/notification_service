package faang.school.notificationservice.service.messageBuilders;

import faang.school.notificationservice.dto.MentorshipOfferedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MentorshipOfferedMessageBuilderTest {
    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private MentorshipOfferedMessageBuilder mentorshipOfferedMessageBuilder;
    @Test
    void testBuildMessage() {
        MentorshipOfferedEvent mentorshipOfferedEvent = new MentorshipOfferedEvent();
        mentorshipOfferedEvent.setAuthorId(1L);
        when(messageSource.getMessage("mentor_request.new", new Object[]{mentorshipOfferedEvent.getAuthorId()}, null))
                .thenReturn("test");
        assertEquals("test", mentorshipOfferedMessageBuilder.buildMessage(mentorshipOfferedEvent, null));
    }

    @Test
    void supportsEventType() {
        assertEquals(MentorshipOfferedEvent.class, mentorshipOfferedMessageBuilder.supportsEventType());
    }
}