package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.MentorshipOfferedEvent;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MentorshipRequestMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private MentorshipRequestMessageBuilder messageBuilder;

    @Test
    public void successBuildMessage() {
        Locale testLocale = new Locale("en");
        MentorshipOfferedEvent event = MentorshipOfferedEvent.builder()
                .requesterId(123)
                .receiverId(456).build();
        when(messageSource.getMessage(eq("mentorship_offered.request"), any(Object[].class), eq(testLocale)))
                .thenReturn("Mentorship offered from 123 to 456");
        String message = messageBuilder.buildMessage(event, testLocale);

        assertEquals("Mentorship offered from 123 to 456", message);
        verify(messageSource, times(1))
                .getMessage("mentorship_offered.request",
                        new Object[]{event.getRequesterId(), event.getReceiverId()}, testLocale);
    }

    @Test
    public void testSupportsEventType() {
        Class<?> supportedClass = messageBuilder.supportsEventType();

        assertEquals(MentorshipOfferedEvent.class, supportedClass);
    }
}