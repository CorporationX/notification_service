package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.ProfileViewEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ProfileViewEventMessageBuilder implements MessageBuilder<ProfileViewEvent>{
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public Class<?> getInstance() {
        return ProfileViewEvent.class;
    }

    @Override
    public String buildMessage(ProfileViewEvent event, Locale locale) {
        String viewerName = userServiceClient.getUser(event.getActorId()).getUsername();
        return messageSource.getMessage("profile.view",
                new Object[]{viewerName}, locale);
    }
}
