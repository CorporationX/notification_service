package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.profile.ProfileViewEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ProfileViewEventMessageBuilder implements MessageBuilder<ProfileViewEvent> {

    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Override
    public String buildMessage(ProfileViewEvent event, Locale locale) {
        UserDto viewer = userServiceClient.getUser(event.getViewerId());
        String defaultMessage = messageSource.getMessage("profile.view", new Object[]{viewer.getUsername()}, Locale.ENGLISH);
        return messageSource.getMessage("profile.view", new Object[]{viewer.getUsername()}, defaultMessage, locale);
    }

    @Override
    public Class<?> getInstance() {
        return ProfileViewEvent.class;
    }
}