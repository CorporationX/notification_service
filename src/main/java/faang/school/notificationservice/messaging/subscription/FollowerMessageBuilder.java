package faang.school.notificationservice.messaging.subscription;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class FollowerMessageBuilder extends AbstractSubscriptionMessageBuilder {

    public FollowerMessageBuilder(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public String getMessageKey() {
        return "follow.message";
    }
}
