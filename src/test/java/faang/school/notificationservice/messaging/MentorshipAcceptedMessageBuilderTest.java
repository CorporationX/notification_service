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
    private String message;

    @BeforeEach
    void setUp() {
        event = new MentorshipAcceptedEvent(1L, "Study of gophers", 2L, "John", 3L,"Mark");
        message = "Congrats, John accept you request Request";
    }

    @Test
    void testBuildMessageWithPlaceholdersSuccess() {
        Object[] placeholders = {"Mark", "John","Study of gophers"};
        String code = "mentorship_accepted.new";
        when(messageSource.getMessage(code, placeholders, Locale.getDefault())).thenReturn(message);

        String result = messageBuilder.buildMessage(event, Locale.getDefault());
        verify(messageSource, times(1)).getMessage(code, placeholders, Locale.getDefault());

        assertEquals(message, result);
    }

    @Test
    void testGetInstanceSuccess() {
        Class<?> instanceClass = messageBuilder.getInstance();

        assertEquals(MentorshipAcceptedEvent.class, instanceClass);
    }
}