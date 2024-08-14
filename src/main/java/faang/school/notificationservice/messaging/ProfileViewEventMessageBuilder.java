package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.ProfileViewEvent;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class ProfileViewEventMessageBuilder implements MessageBuilder<ProfileViewEvent> {
    @Override
    public Class<?> getInstance() {
        return ProfileViewEvent.class;
    }

    @Override
    public String buildMessage(ProfileViewEvent event, Locale locale) {
        return " viewed your profile!";
    }
}
