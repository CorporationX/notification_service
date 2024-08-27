package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.events.MentorshipAcceptedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipAcceptedMessageBuilder implements MessageBuilder<MentorshipAcceptedEvent> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public Class<?> getInstance() {
        return this.getClass();
    }

    @Override
    public String buildMessage(MentorshipAcceptedEvent event, Locale locale) {
        UserDto dto = userServiceClient.getUser(event.getRequesterId());
        Object[] obj = {dto.getUsername()};
        return messageSource.getMessage("mentorship.accept", obj, locale);
    }

    @Override
    public Class<?> supportsEvent() {
        return MentorshipAcceptedEvent.class;
    }
}