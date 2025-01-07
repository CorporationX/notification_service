package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.SubscriptionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class SubscriptionMessageBuilder implements MessageBuilder<SubscriptionEvent> {

    private final MessageSource messageSource;

    @Value("${spring.messages.code.subscription}")
    private String messageCode;

    @Override
    public Class<SubscriptionEvent> getInstance() {
        return SubscriptionEvent.class;
    }

    @Override
    public String buildMessage(SubscriptionEvent event, Locale locale) {
        Object[] placeholders = {event.followeeName(), event.followerName(), event.subscribedAt()};
        return messageSource.getMessage(messageCode, placeholders, locale);
    }
}
