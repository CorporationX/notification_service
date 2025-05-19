package faang.school.notificationservice.messaging.message2.builder2;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowEventDto;
import faang.school.notificationservice.dto.UserDto;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class FollowEventMessageBuilder extends AbstractMessageBuilder<FollowEventDto> {
    private final UserServiceClient userServiceClient;

    public FollowEventMessageBuilder(Locale defaultLocale, MessageSource messageSource, UserServiceClient userServiceClient) {
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
