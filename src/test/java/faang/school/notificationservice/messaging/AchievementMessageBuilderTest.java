package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.AchievementEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementMessageBuilderTest {

    @InjectMocks
    private AchievementMessageBuilder builder;

    @Mock
    private MessageSource messageSource;

    @Test
    public void testGetInstance() {
        Class<?> clazz = builder.getInstance();
        assertEquals(AchievementEvent.class, clazz);
    }

    @Test
    public void testBuildMessage() {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setLocale(new Locale("ru", "RU"));

        AchievementEvent event = new AchievementEvent();

        String text = "text";

        when(messageSource.getMessage(any(), any(), any())).thenReturn(text);

        String result = builder.buildMessage(event, user.getLocale());

        assertEquals(text, result);
    }
}
