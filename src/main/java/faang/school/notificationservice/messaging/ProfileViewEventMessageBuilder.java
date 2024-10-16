package faang.school.notificationservice.messaging;

import faang.school.notificationservice.messaging.dto.ProfileViewEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ProfileViewEventMessageBuilder implements MessageBuilder<ProfileViewEventDto> {
    private static final String MESSAGE_KEY = "user.view";

    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return ProfileViewEventDto.class;
    }

    @Override
    public String buildMessage(ProfileViewEventDto event, Locale locale) {
        return messageSource.getMessage(MESSAGE_KEY, new Object[]{"id: " + event.getReceiverId()}, locale);
    }
}
