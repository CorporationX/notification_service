package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.event.AchievementEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementEvenBuilderTest {

    @InjectMocks
    private AchievementEvenBuilder achievementEvenBuilder;

    @Mock
    private MessageSource messageSource;

    private static final String EXPECTED_MESSAGE = "Congrats! You've got a new achievement!";
    private static final String PROPERTY_NAME = "achievement";

    @Test
    @DisplayName("Успешное возвращение AchievementEvent.class")
    public void whenGetInstanceReturnLikePostEvent() {
        assertEquals(AchievementEvent.class, achievementEvenBuilder.getInstance());
    }

    @Test
    @DisplayName("Успешное построение сообщения")
    public void whenBuildMessageReturnExpectedMessage() {
        AchievementEvent event = AchievementEvent.builder().build();
        Locale locale = Locale.UK;
        when(messageSource.getMessage(PROPERTY_NAME, new Object[]{}, locale)).thenReturn(EXPECTED_MESSAGE);

        String actualMessage = achievementEvenBuilder.buildMessage(event, locale);

        assertEquals(EXPECTED_MESSAGE, actualMessage);
        verify(messageSource).getMessage(PROPERTY_NAME, new Object[]{}, locale);
    }
}