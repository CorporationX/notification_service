package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.achievement.AchievementEventDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementReceivedMessageBuilderTest {
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private AchievementReceivedMessageBuilder messageBuilder;

    @Test
    public void testBuildMessage() {
        AchievementEventDto achievementEventDto = AchievementEventDto.builder()
                .authorId(1L)
                .achievementId(2L)
                .title("Test Achievement Title")
                .build();

        Locale locale = Locale.US;
        String expectedMessage = String.format("Hello, you got an achievement %s!", achievementEventDto.getTitle());

        when(messageSource.getMessage(eq("achievement.received"),
                any(Object[].class), eq(locale)))
                .thenReturn(expectedMessage);

        String actualMessage = messageBuilder.buildMessage(achievementEventDto, locale);

        assertEquals(expectedMessage, actualMessage);
    }
}