package faang.school.notificationservice.messaging.subscription;

import faang.school.notificationservice.dto.subscription.SubscriptionEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
public abstract class AbstractSubscriptionMessageBuilder implements MessageBuilder<SubscriptionEventDto> {

    protected final MessageSource messageSource;

    protected AbstractSubscriptionMessageBuilder(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    protected abstract String getMessageKey();

    @Override
    public String buildMessage(SubscriptionEventDto eventDto, Locale locale) {
        String eventTimeFormatted = eventDto.getEventTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", locale));
        Object[] args = new Object[]{eventDto.getFollowerId(), eventTimeFormatted};
        return messageSource.getMessage(getMessageKey(), args, locale);
    }

    @Override
    public Class<SubscriptionEventDto> getInstance() {
        return SubscriptionEventDto.class;
    }
}
