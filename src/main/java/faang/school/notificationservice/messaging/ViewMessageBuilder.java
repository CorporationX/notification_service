package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.ProfileViewEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ViewMessageBuilder implements MessageBuilder<ProfileViewEvent> {
    private final MessageSource messageSource;

    @Override
    public String buildMessage(ProfileViewEvent event, Locale locale) {
        return messageSource.getMessage("profile.view", new Object[]{event.getViewerId(), event.getViewedId()}, locale);
    }

    @Override
    public Class<ProfileViewEvent> getInstance() {
        return ProfileViewEvent.class;
    }
}
