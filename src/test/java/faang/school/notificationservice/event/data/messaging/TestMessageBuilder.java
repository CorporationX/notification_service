package faang.school.notificationservice.event.data.messaging;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import org.springframework.boot.test.context.TestComponent;

import java.util.Locale;

@TestComponent
public class TestMessageBuilder implements MessageBuilder<UserDto> {
    @Override
    public Class<UserDto> getInstance() {
        return UserDto.class;
    }

    @Override
    public String buildMessage(UserDto event, Locale locale) {
        return event.getClass().getName();
    }
}
