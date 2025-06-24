package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.ProfileViewEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ProfileViewMessageBuilder implements MessageBuilder<ProfileViewEventDto> {

    private static final String MESSAGE_KEY = "notification.profile.view";

    private final MessageSource messageSource;

    @Override
    public Class<ProfileViewEventDto> getInstance() {
        return ProfileViewEventDto.class;
    }

    @Override
    public String buildMessage(ProfileViewEventDto event, Locale locale) {
        return messageSource.getMessage(
                MESSAGE_KEY,
                new Object[]{event.getViewerId()},
                locale
        );
    }
}
