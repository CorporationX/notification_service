package faang.school.notificationservice.messaging.message_builder;

import faang.school.notificationservice.dto.ProfileViewEvent;
import faang.school.notificationservice.messaging.message_builder.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

import java.util.Locale;

@RequiredArgsConstructor
public class ProfileViewMessageBuilder implements MessageBuilder<ProfileViewEvent> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(ProfileViewEvent eventType, Locale locale) {
        return messageSource.getMessage("profile_view.new", new Object[]{eventType.getProfileViewedId()}, locale);
    }

    @Override
    public boolean supportsEventType(ProfileViewEvent eventType) {
        return eventType != null;
    }
}
