package faang.school.notificationservice.messaging.mentorship;

import faang.school.notificationservice.event.mentorship.request.MentorshipAcceptedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipAcceptedMessageBuilder implements MessageBuilder<MentorshipAcceptedEvent> {

    private final MessageSource messageSource;
    private final UserService userService;

    @Override
    public Class<MentorshipAcceptedEvent> getInstance() {
        return MentorshipAcceptedEvent.class;
    }

    @Override
    public String buildMessage(MentorshipAcceptedEvent event, Locale locale) {
        var receiver = userService.fetchUser(event.getRequesterId());
        return messageSource.getMessage("mentorship.accepted", new Object[]{receiver.getUsername()}, locale);
    }

}
