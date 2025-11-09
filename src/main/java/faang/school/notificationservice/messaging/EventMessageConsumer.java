package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.ResourceBundle;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventMessageConsumer implements MessageBuilder<UserDto> {
    @Override
    public Class<?> getInstance() {
        return null;
    }

    @Override
    public String buildMessage(UserDto userDto, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Уважаемый ")
                .append(userDto.getUsername())
                .append(bundle.getString("event.start"));

        return stringBuilder.toString();
    }
}
