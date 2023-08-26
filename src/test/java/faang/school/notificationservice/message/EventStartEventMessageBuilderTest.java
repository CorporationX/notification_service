package faang.school.notificationservice.message;

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

        eventStartEventMessageBuilder.buildMessage(Locale.CHINA, "Halloween");

        verify(messageSource).getMessage("event.start", new Object[]{"Halloween"}, Locale.CHINA);
    }
}