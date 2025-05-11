package faang.school.notificationservice.messaging.subscription;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class FollowerMessageBuilder extends AbstractSubscriptionMessageBuilder {

    private static final String FOLLOW_CODE = "follow.message";

    public FollowerMessageBuilder(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public String getMessageKey() {
        return FOLLOW_CODE;
    }
}
