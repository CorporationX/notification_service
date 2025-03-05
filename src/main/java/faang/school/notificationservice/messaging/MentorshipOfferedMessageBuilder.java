package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.context.UserContext;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.MentorshipOfferedEvent;
import faang.school.notificationservice.listener.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

import static faang.school.notificationservice.listener.EventType.EVENT_TYPE_RECOMMENDATION;

@Component
@RequiredArgsConstructor
public class MentorshipOfferedMessageBuilder implements MessageBuilder<MentorshipOfferedEvent> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;
    private final UserContext userContext;

    @Override
    public EventType getEventType() {
        return EVENT_TYPE_RECOMMENDATION;
    }

    @Override
    public String buildMessage(MentorshipOfferedEvent event, Locale locale) {
        userContext.setUserId(event.getRequesterId());
        UserDto requesterDto = userServiceClient.getUser(event.getRequesterId());
        UserDto mentorDto = userServiceClient.getUser(event.getMentorId());
        return messageSource.getMessage("mentorship.offered",
                new Object[]{mentorDto.getUsername(), requesterDto.getUsername()}, locale);
    }
}
