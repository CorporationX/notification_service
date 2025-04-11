package faang.school.notificationservice.messaging.subscription;

import faang.school.notificationservice.dto.subscription.SubscriptionEventDto;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class FollowerMessageBuilder extends AbstractSubscriptionMessageBuilder {

    public FollowerMessageBuilder(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    protected String getMessageKey() {
        return "follow.message";
    }

    @Override
    protected Object[] getArguments(SubscriptionEventDto eventDto) {
        return new Object[]{eventDto.getFollowerId()};
    }
}
