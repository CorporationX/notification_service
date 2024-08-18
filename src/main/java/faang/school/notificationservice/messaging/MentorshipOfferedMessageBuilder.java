package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.events.MentorshipOfferedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipOfferedMessageBuilder implements MessageBuilder<MentorshipOfferedEvent> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public Class<?> getInstance() {
        return this.getClass();
    }

    @Override
    public String buildMessage(MentorshipOfferedEvent event, Locale locale) {
        UserDto dto = userServiceClient.getUser(event.getAuthorId());
        Object[] obj = {dto.getUsername()};
        return messageSource.getMessage("mentorship.offer", obj, locale);
    }

    @Override
    public boolean supportsEvent(MentorshipOfferedEvent event) {
        return MentorshipOfferedEvent.class == event.getClass();
    }
}
