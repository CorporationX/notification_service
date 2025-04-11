package faang.school.notificationservice.messaging.subscription;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class UnfollowerMessageBuilder extends AbstractSubscriptionMessageBuilder {

    public UnfollowerMessageBuilder(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public String getMessageKey() {
        return "unfollow.message";
    }
}
