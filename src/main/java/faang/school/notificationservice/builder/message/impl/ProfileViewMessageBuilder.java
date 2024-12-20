package faang.school.notificationservice.builder.message.impl;

import faang.school.notificationservice.builder.message.MessageBuilder;
import faang.school.notificationservice.config.messageSource.MessageKeys;
import faang.school.notificationservice.message.event.ProfileViewEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ProfileViewMessageBuilder implements MessageBuilder<ProfileViewEvent> {

    private final MessageKeys messageKeys;
    private final MessageSource messageSource;

    @Override
    public String build(ProfileViewEvent event, Locale locale) {
        return messageSource.getMessage(
                messageKeys.getProfileView(),
                null,
                locale);
    }
}
