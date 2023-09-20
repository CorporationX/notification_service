package faang.school.notificationservice.messaging.message_builder;

import faang.school.notificationservice.client.service.UserServiceClient;
import faang.school.notificationservice.dto.redis.ProfileViewEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class ProfileViewMessageBuilder extends AbstractMessageBuilder
        implements MessageBuilder<ProfileViewEvent> {
    @Autowired
    public ProfileViewMessageBuilder(UserServiceClient userServiceClient, MessageSource messageSource) {
        super(userServiceClient, messageSource);
    }

    @Override
    public String buildMessage(ProfileViewEvent eventType, Locale locale) {
        String viewerName = userServiceClient.getUser(eventType.getProfileViewedId()).getUsername();
        return messageSource.getMessage("profile_view.new", new Object[]{viewerName}, locale);
    }
}
