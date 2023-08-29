package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.EventStartEventDto;
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
        UserDto userDto = UserDto.builder()
                .username("Hooks")
                .locale(Locale.CHINA)
                .build();
        EventStartEventDto eventDto = EventStartEventDto.builder().
                title("Halloween").
                build();

        eventStartEventMessageBuilder.buildMessage(userDto, eventDto);

        verify(messageSource).getMessage("event.start", new Object[]{"Hooks", "Halloween"}, userDto.getLocale());
    }
}