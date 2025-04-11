package faang.school.notificationservice.messaging.subscription;

import faang.school.notificationservice.dto.subscription.SubscriptionEventDto;
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
