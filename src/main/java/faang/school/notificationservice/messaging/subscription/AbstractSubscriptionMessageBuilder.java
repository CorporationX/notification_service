package faang.school.notificationservice.messaging.subscription;

import faang.school.notificationservice.dto.subscription.SubscriptionEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import org.springframework.context.MessageSource;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;

public abstract class AbstractSubscriptionMessageBuilder implements MessageBuilder<SubscriptionEventDto> {

    protected final MessageSource messageSource;

    protected AbstractSubscriptionMessageBuilder(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    protected abstract String getMessageKey();

    protected Object[] getArguments(SubscriptionEventDto eventDto) {
        return new Object[]{eventDto.getFollowerId()};
    }

    @Override
    public String buildMessage(SubscriptionEventDto eventDto, Locale locale) {
        String eventTimeFormatted = eventDto.getEventTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", locale));
        Object[] args = getArguments(eventDto);
        args = Arrays.copyOf(args, args.length + 1);
        args[args.length - 1] = eventTimeFormatted;
        return messageSource.getMessage(getMessageKey(), args, locale);
    }

    @Override
    public Class<?> getInstance() {
        return SubscriptionEventDto.class;
    }
}
