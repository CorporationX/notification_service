package faang.school.notificationservice.messaging.messagebuilder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowEventDto;
import faang.school.notificationservice.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class FollowEventMessageBuilder extends AbstractMessageBuilder<FollowEventDto> {
    private final UserServiceClient userServiceClient;

    public FollowEventMessageBuilder(
            @Value("${app.default-locale}") String defaultLocale,
            MessageSource messageSource,
            UserServiceClient userServiceClient) {
        super(defaultLocale, messageSource);
        this.userServiceClient = userServiceClient;
    }

    @Override
    public Class<?> getInstance() {
        return FollowEventDto.class;
    }

    @Override
    protected String buildMessageWithLocale(FollowEventDto event, Locale locale) {
        UserDto user = userServiceClient.getUser(event.followerId());
        return getMessageSource().getMessage("follow.notification", new Object[]{user.getUsername()}, locale);
    }


}
