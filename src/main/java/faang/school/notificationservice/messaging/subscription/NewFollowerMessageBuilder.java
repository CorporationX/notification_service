package faang.school.notificationservice.messaging.subscription;

import faang.school.notificationservice.event.kafka.NewFollowerEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@RequiredArgsConstructor
@Component
public class NewFollowerMessageBuilder implements MessageBuilder<NewFollowerEvent> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(NewFollowerEvent event, Locale locale) {
        String followerName = event.getFollower().getUsername();
        String messageTemplateCode = "subscription.subscribe";
        return messageSource.getMessage(messageTemplateCode, new Object[]{followerName}, locale);
    }

    @Override
    public Class<?> getInstance() {
        return NewFollowerEvent.class;
    }
}