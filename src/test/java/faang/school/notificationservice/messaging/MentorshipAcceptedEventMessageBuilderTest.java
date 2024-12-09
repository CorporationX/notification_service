package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.MentorshipAcceptedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MentorshipAcceptedEventMessageBuilderTest {

    @Spy
    private MessageSource messageSource;

    @InjectMocks
    private MentorshipAcceptedEventMessageBuilder builder;

    @Test
    void supportEventTypeTest() {
        Class<?> expectedClass = MentorshipAcceptedEvent.class;
        Class<?> actualClass = assertDoesNotThrow(() -> builder.supportEventType());
        assertEquals(expectedClass, actualClass);
    }

    @Test
    void buildMessageTest() {
        MentorshipAcceptedEvent event = new MentorshipAcceptedEvent();
        String receiverUsername = "perec";
        event.setReceiverUsername(receiverUsername);
        Locale locale = Locale.UK;
        String expectedMessage = "Congrats! Your request to the user with ID 2 for mentoring has been accepted!";
        String messageCode = "mentorship.accepted";
        when(messageSource.getMessage(messageCode, new Object[] {receiverUsername}, locale)).thenReturn(expectedMessage);

        String builtMessage = assertDoesNotThrow(() -> builder.buildMessage(event, locale));

        assertEquals(expectedMessage, builtMessage);
    }
}