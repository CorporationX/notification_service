package faang.school.notificationservice.messaging.mentorship;

import faang.school.notificationservice.event.mentorship.request.MentorshipOfferedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipOfferedMessageBuilder implements MessageBuilder<MentorshipOfferedEvent> {
    private final MessageSource messageSource;
    private final UserService userService;

    @Override
    public Class<MentorshipOfferedEvent> getInstance() {
        return MentorshipOfferedEvent.class;
    }

    @Override
    public String buildMessage(MentorshipOfferedEvent event, Locale locale) {
        var receiver = userService.fetchUser(event.getRequesterId());
        return messageSource.getMessage("mentorship.offered",
                new Object[]{receiver.getUsername()}, locale);
    }

}
