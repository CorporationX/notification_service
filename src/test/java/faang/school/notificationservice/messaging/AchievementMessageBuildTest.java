package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.AchievementEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementMessageBuildTest {
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private AchievementMessageBuilder achievementMessageBuilder;

    private AchievementEvent event;
    private Locale locale;

    @BeforeEach
    void setUp() {
        event = AchievementEvent.builder().userId(1L).achievementId(1L).build();
        locale = Locale.ENGLISH;
    }

    @Test
    void  testBuildMessageWithPlaceholdersSuccess() {
        String expectedMessage = "Achievement unlocked by user 1 for achievement 1";
        when(messageSource.getMessage(eq("achievement.add"), any(), eq(locale)))
                .thenReturn(expectedMessage);

        String actualMessage = achievementMessageBuilder.buildMessage(event, locale);

        assertEquals(expectedMessage, actualMessage);

        verify(messageSource, times(1)).getMessage(eq("achievement.add"), any(), eq(locale));
    }

    @Test
    @DisplayName("getInstance return success")
    void testGetInstance_Success() {
        AchievementMessageBuilder messageBuilder = new AchievementMessageBuilder(null);

        Class<?> result = messageBuilder.getInstance();

        assertNotNull(result);
    }
}
