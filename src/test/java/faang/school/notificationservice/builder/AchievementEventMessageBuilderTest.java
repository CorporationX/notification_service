package faang.school.notificationservice.builder;

import faang.school.notificationservice.messaging.AchievementEventMessageBuilder;
import faang.school.notificationservice.model.event.AchievementEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AchievementEventMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private AchievementEventMessageBuilder builder;

    @Test
    void testBuilderOk(){
        AchievementEvent event = AchievementEvent.builder()
                .userId(1L)
                .achievementId(2L)
                .title("hello")
                .build();

        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("hello there");

        assertEquals("hello there", builder.buildMessage(event, Locale.getDefault()));
    }
}
