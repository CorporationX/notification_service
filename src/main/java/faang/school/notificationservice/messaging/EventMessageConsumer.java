package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.EventStartEventDto;
import faang.school.notificationservice.dto.TimeLeft;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventMessageConsumer implements MessageBuilder<EventStartEventDto> {
    @Override
    public Class<?> getInstance() {
        return null;
    }

    @Override
    public String buildMessage(EventStartEventDto eventStartEventDto, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        String text = bundle.getString(eventStartEventDto.timeLeft().getMessageKey());

        return MessageFormat.format(text, eventStartEventDto.titleEvent(), eventStartEventDto.nameOwner());
    }
}
