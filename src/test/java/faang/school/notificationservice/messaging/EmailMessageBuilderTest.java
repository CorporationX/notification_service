package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.EmailEvent;
import faang.school.notificationservice.mapper.EmailEventMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailMessageBuilderTest {

    private final String eventSubject = "event.test";
    @InjectMocks
    private EmailMessageBuilder emailMessageBuilder;

    @Spy
    private EmailEventMapper emailEventMapper;

    @Mock
    private MessageSource messageSource;

    @Spy
    private EmailEvent emailEvent;

    @Spy
    private SimpleMailMessage simpleMailMessage;

    private final String from = "test@domain.com";
    private final Locale locale = Locale.ENGLISH;

    @BeforeEach
    void setUp() {
        emailEvent.setSubject(eventSubject);
        ReflectionTestUtils.setField(emailMessageBuilder, "from", from);
    }

    @Test
    void buildMessage_Success() {
        String subject = "Test Subject", text = "Test Text";
        SimpleMailMessage correctResult = new SimpleMailMessage();
        correctResult.setFrom(from);
        correctResult.setSubject(subject);
        correctResult.setText(text);

        when(emailEventMapper.toSimpleMailMessage(emailEvent)).thenReturn(simpleMailMessage);
        when(messageSource.getMessage(eventSubject + ".subject", null, locale)).thenReturn(subject);
        when(messageSource.getMessage(eventSubject, null, locale)).thenReturn(text);

        SimpleMailMessage result = emailMessageBuilder.buildMessage(emailEvent, locale);

        verify(emailEventMapper).toSimpleMailMessage(emailEvent);
        verify(messageSource).getMessage(eventSubject + ".subject", null, locale);
        verify(messageSource).getMessage(eventSubject, null, locale);

        assertEquals(correctResult, result);
    }

    @Test
    void buildMessage_WithNullSubject_ThrowsException() {
        when(messageSource.getMessage(eventSubject + ".subject", null, locale)).thenThrow(NoSuchMessageException.class);

        assertThrows(NoSuchMessageException.class, () -> emailMessageBuilder.buildMessage(emailEvent, locale));
    }
}
