package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.ProfileViewedEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

import java.util.Locale;

@RequiredArgsConstructor
public class ProfileViewedMessageBuilder implements MessageBuilder<ProfileViewedEventDto> {

    private static final String PROFILE_VIEWED = "profile.viewed";

    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return ProfileViewedEventDto.class;
    }

    @Override
    public String buildMessage(ProfileViewedEventDto event, Locale locale) {
        Object[] args = {event.getViewedId(), event.getViewerName(), event.getViewerId()};
        return messageSource.getMessage(PROFILE_VIEWED, args, locale);
    }
}
