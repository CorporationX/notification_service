package faang.school.notificationservice.messaging.profile;

import faang.school.notificationservice.dto.profile.ProfileViewEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ProfileViewMessageBuilder implements MessageBuilder<ProfileViewEvent> {

    @Value("profile_view.new")
    private String profileViewKey;
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return ProfileViewEvent.class;
    }

    @Override
    public String buildMessage(ProfileViewEvent event, Locale locale) {
        return messageSource.getMessage(profileViewKey,
                new Object[]{event.getViewerId(),
                        event.getVisitingTime()}, locale);
    }
}
