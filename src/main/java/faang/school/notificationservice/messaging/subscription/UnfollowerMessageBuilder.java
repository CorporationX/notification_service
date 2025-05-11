package faang.school.notificationservice.messaging.subscription;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class UnfollowerMessageBuilder extends AbstractSubscriptionMessageBuilder {

    private static final String UNFOLLOW_CODE = "unfollow.message";

    public UnfollowerMessageBuilder(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public String getMessageKey() {
        return UNFOLLOW_CODE;
    }
}
