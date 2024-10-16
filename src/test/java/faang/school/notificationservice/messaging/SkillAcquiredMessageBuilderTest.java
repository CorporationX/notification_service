package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.SkillAcquiredEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkillAcquiredMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private SkillAcquiredMessageBuilder builder;

    @Test
    void testOkBuilder() {
        // given
        SkillAcquiredEvent event = SkillAcquiredEvent.builder().build();
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("hello there");
        // when & then
        assertEquals("hello there", builder.buildMessage(event, Locale.getDefault()));
    }
}