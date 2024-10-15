package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.ProfileViewEvent;
import faang.school.notificationservice.service.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ProfileViewMessageBuilder implements MessageBuilder<ProfileViewEvent> {
    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Override
    public Class<ProfileViewEvent> getSupportedClass() {
        return ProfileViewEvent.class;
    }

    @Override
    public String buildMessage(ProfileViewEvent event, Locale locale) {
        UserDto profileOwnerDto = userServiceClient.getUser(event.getProfileOwnerId());
        UserDto profileViewerDto = userServiceClient.getUser(event.getViewerId());
        return messageSource.getMessage("profile.view",
                new Object[]{profileOwnerDto.getUsername(), profileViewerDto.getUsername()}, locale);
    }
}
