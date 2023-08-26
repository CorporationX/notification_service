package faang.school.notificationservice.messageBuilder;

import faang.school.notificationservice.dto.EventCountdown;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventStartEventMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private EventStartEventMessageBuilder eventStartEventMessageBuilder;

    @Test
    void buildMessageTest() {
        String message = "event.countdown." + EventCountdown.DAY;

        eventStartEventMessageBuilder.buildCustomMessage(Locale.CHINA, EventCountdown.DAY, "Halloween");

        verify(messageSource).getMessage(message, new Object[]{"Halloween"}, Locale.CHINA);
    }
}