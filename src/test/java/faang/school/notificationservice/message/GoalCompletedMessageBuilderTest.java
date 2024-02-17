package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.GoalCompletedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoalCompletedMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private GoalCompletedMessageBuilder messageBuilder;

    @BeforeEach
    void setUp() {
        LocaleContextHolder.setLocale(Locale.US);
    }

    @Test
    void buildMessageReturnsCorrectFormattedMessage() {
        String username = "TestUser";
        GoalCompletedEvent event = new GoalCompletedEvent();
        String expectedMessage = "Hello, TestUser! You have completed goal.";

        when(messageSource.getMessage(eq("goal_completed.new"), any(), any(Locale.class)))
                .thenReturn(expectedMessage);

        String actualMessage = messageBuilder.buildMessage(event, username);

        assertEquals(expectedMessage, actualMessage);
    }
}
