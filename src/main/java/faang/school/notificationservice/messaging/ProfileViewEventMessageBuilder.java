package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ProfileViewEventDto;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ProfileViewEventMessageBuilder implements MessageBuilder<ProfileViewEventDto> {

    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return ProfileViewEventDto.class;
    }

    @Override
    public String buildMessage(ProfileViewEventDto event, Locale locale) {
        UserDto user = userServiceClient.getUser(event.getViewerId());
        return messageSource.getMessage("profile.view", new Object[]{user.getUsername()}, locale);
    }
}
