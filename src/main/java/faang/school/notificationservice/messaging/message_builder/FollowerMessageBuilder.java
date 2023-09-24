package faang.school.notificationservice.messaging.message_builder;

import faang.school.notificationservice.client.service.UserServiceClient;
import faang.school.notificationservice.dto.redis.FollowerEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class FollowerMessageBuilder extends AbstractMessageBuilder
        implements MessageBuilder<FollowerEvent> {
    @Autowired
    public FollowerMessageBuilder(UserServiceClient userServiceClient, MessageSource messageSource) {
        super(userServiceClient, messageSource);
    }

    @Override
    public String buildMessage(FollowerEvent event, Locale locale) {
        String followerName = userServiceClient.getUser(event.getFollowerId()).getUsername();
        return messageSource.getMessage("follower.new", new String[]{followerName}, locale);
    }
}
