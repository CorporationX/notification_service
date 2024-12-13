package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.LikeEvent;
import faang.school.notificationservice.event.MentorshipAcceptedEvent;
import faang.school.notificationservice.event.RecommendationReceivedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class MentorshipAcceptedMessageBuilderTest {
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private MentorshipAcceptedMessageBuilder messageBuilder;

    private MentorshipAcceptedEvent event;

    @BeforeEach
    void setUp() {
        event = new MentorshipAcceptedEvent(1L, "Request", 2L, "John", 3L);
    }

    @Test
    void testBuildMessageWithPlaceholdersSuccess() {
        Object[] placeholders = {"John", "Request"};
        String code = "mentorship_accepted.new";
        when(messageSource.getMessage(code, placeholders, Locale.getDefault())).thenReturn("Congrats, John accept you request Request");

        String result = messageBuilder.buildMessage(event, Locale.getDefault());
        verify(messageSource, times(1)).getMessage(code, placeholders,Locale.getDefault());

        assertEquals("Congrats, John accept you request Request", result);
    }

    @Test
    void testGetInstanceSuccess() {
        Class<?> instanceClass = messageBuilder.getInstance();

        assertEquals(MentorshipAcceptedEvent.class, instanceClass);
    }
}