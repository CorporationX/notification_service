package faang.school.notificationservice.service.message_builder;

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
    public String buildMessage(ProfileViewEvent event, Locale locale) {
        return messageSource.getMessage("follower.new", new Object[]{}, locale);
    }

    @Override
    public long getReceiverId(ProfileViewEvent event) {
        return event.getViewedUserId();
    }
}