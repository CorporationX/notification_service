package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.kafka.SubscriptionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@RequiredArgsConstructor
@Component
public class SubscriptionMessageBuilder implements MessageBuilder<SubscriptionEvent> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(SubscriptionEvent event, Locale locale) {
        String followerName = event.getFollower().getUsername();
        String messageTemplateCode;
        switch (event.getSubscriptionEventType()) {
            case SUBSCRIBE -> messageTemplateCode = "subscription.subscribe";
            case UNSUBSCRIBE -> messageTemplateCode = "subscription.unsubscribe";
            default -> throw new RuntimeException();
        }
        return messageSource.getMessage(messageTemplateCode, new Object[]{followerName}, locale);
    }

    @Override
    public Class<?> getInstance() {
        return SubscriptionEvent.class;
    }
}