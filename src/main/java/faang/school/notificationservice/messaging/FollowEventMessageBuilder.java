package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowEventDto;
import faang.school.notificationservice.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class FollowEventMessageBuilder implements MessageBuilder<FollowEventDto> {
    private final Locale defaultLocale;
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Autowired
    public FollowEventMessageBuilder(
            @Value("${app.default-locale}") String defaultLocaleStr,
            MessageSource messageSource,
            UserServiceClient userServiceClient
    ) {
        this.defaultLocale = new Locale(defaultLocaleStr);
        this.messageSource = messageSource;
        this.userServiceClient = userServiceClient;
    }

    @Override
    public Class<?> getInstance() {
        return FollowEventDto.class;
    }

    @Override
    public String buildMessage(FollowEventDto event, Locale locale) {
        return buildMessageWithLocale(event, locale != null ? locale : defaultLocale);
    }

    private String buildMessageWithLocale(FollowEventDto event, Locale locale) {
        UserDto user = userServiceClient.getUser(event.followerId());
        return messageSource.getMessage("follow.notification", new Object[]{user.getUsername()}, locale);
    }
}
