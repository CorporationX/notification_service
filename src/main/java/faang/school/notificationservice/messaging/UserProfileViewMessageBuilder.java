package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.UserProfileViewEvent;
import faang.school.notificationservice.listener.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;


import java.util.Locale;

@Component
@RequiredArgsConstructor
public class UserProfileViewMessageBuilder implements MessageBuilder<UserProfileViewEvent>{

    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    public Class<?> getInstance() {
        return UserProfileViewEvent.class;
    }

    @Override
    public EventType getEventType() {
        return EventType.EVENT_TYPE_PROFILE_VIEW;
    }

    @Override
    public String buildMessage(UserProfileViewEvent event, Locale locale) {
        UserDto visitorDto = userServiceClient.getUser(event.visitorUserId());
        return messageSource.getMessage("user.view_profile", new Object[]{visitorDto.getUsername()}, locale);
    }
}
