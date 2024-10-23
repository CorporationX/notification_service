package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.skill.SkillAcquiredEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkillAcquiredBuilderTest {
    private static final String TEST = "TEST";

    @InjectMocks
    private SkillAcquiredBuilder skillAcquiredBuilder;

    @Mock
    private MessageSource messageSource;

    @Test
    @DisplayName("should return SkillAcquiredEvent.class")
    void whenCalledMethodThenReturnGoalCompletedEventClass() {
        assertEquals(SkillAcquiredEvent.class, skillAcquiredBuilder.getInstance());
    }

    @Test
    @DisplayName("should return ExpectedMessage")
    void whenCalledMethodThenReturnExpectedMessage() {
        SkillAcquiredEvent event = SkillAcquiredEvent.builder()
                .skillId(1L)
                .receiverId(2L)
                .skillTitle(TEST)
                .build();

        Locale locale = Locale.ENGLISH;

        when(messageSource.getMessage("skill.acquired", new Object[]{event.getSkillTitle()}, locale))
                .thenReturn(TEST);

        String message = skillAcquiredBuilder.buildMessage(event, locale);

        assertEquals(TEST, message);
        verify(messageSource).getMessage("skill.acquired", new Object[]{event.getSkillTitle()}, locale);
    }
}
