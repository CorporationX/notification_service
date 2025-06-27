package faang.school.notificationservice.messaging.subscription;

import faang.school.notificationservice.event.kafka.UnfollowEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@RequiredArgsConstructor
@Component
public class UnfollowMessageBuilder implements MessageBuilder<UnfollowEvent> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(UnfollowEvent event, Locale locale) {
        String followerName = event.getFollower().getUsername();
        String messageTemplateCode = "subscription.unsubscribe";
        return messageSource.getMessage(messageTemplateCode, new Object[]{followerName}, locale);
    }

    @Override
    public Class<?> getInstance() {
        return UnfollowEvent.class;
    }
}