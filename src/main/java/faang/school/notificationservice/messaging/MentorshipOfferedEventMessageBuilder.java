package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.MentorshipOfferedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Slf4j
@RequiredArgsConstructor
public class MentorshipOfferedEventMessageBuilder implements MessageBuilder{

    private final ResourceBundleMessageSource messageSource;

    @Value("${spring.messages.builder.event.mentorship-offered}")
    private String messagePath;

    @Override
    public Class<?> getInstance() {
        return MentorshipOfferedEvent.class;
    }

    @Override
    public String buildMessage(UserDto userDto, Locale locale) {
        return messageSource.getMessage(messagePath, new Object[]{userDto.getUsername()}, locale);
    }
}
