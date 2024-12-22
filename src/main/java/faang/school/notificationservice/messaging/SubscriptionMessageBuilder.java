package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.SubscriptionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class SubscriptionMessageBuilder implements MessageBuilder<SubscriptionEvent> {
    private static final String CODE = "subscription.new";
    private final MessageSource messageSource;

    @Override
    public Class<SubscriptionEvent> getInstance() {
        return SubscriptionEvent.class;
    }

    @Override
    public String buildMessage(SubscriptionEvent event, Locale locale) {
        Object[] placeholders = {event.getFolloweeName(), event.getFollowerName(), event.getSubscribedAt()};
        return messageSource.getMessage(CODE, placeholders, locale);
    }
}
