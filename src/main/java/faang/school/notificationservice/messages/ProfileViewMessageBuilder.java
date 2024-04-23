package faang.school.notificationservice.messages;


import faang.school.notificationservice.dto.ProfileViewEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ProfileViewMessageBuilder implements MessageBuilder<ProfileViewEvent> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(ProfileViewEvent event, Locale userLocale) {
        return messageSource.getMessage("profile_view_event", new Object[]{event.getObserverId()}, userLocale);
    }

    @Override
    public boolean supportsEventType(Class<?> eventType) {
        return ProfileViewEvent.class == eventType;
    }

}
