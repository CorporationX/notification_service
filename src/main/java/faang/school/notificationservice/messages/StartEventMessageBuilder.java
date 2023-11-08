package faang.school.notificationservice.messages;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.EventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class StartEventMessageBuilder implements MessageBuilder<EventDto>{

    private final MessageSource messageSource;
    @Override
    public String buildMessage(EventDto eventDto, Locale locale) {
        String eventTitle = eventDto.getTitle();
        return messageSource.getMessage("start_event_topic", new Object[]{eventTitle}, locale);
    }

    @Override
    public boolean supportsEventType(Class eventType) {
        return false;
    }
}
