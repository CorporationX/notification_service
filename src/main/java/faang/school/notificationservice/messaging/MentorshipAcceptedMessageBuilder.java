package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.context.UserContext;
import faang.school.notificationservice.config.message_source.MessageProperties;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.MentorshipAcceptedEvent;
import faang.school.notificationservice.listener.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipAcceptedMessageBuilder implements MessageBuilder<MentorshipAcceptedEvent> {

    private final MessageProperties messageProperties;
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;
    private final UserContext userContext;

    @Override
    public EventType getEventType() {
        return EventType.EVENT_TYPE_MENTORSHIP_ACCEPTED;
    }

    @Override
    public String buildMessage(MentorshipAcceptedEvent event, Locale locale) {
        userContext.setUserId(event.requesterId());
        UserDto requesterDto = userServiceClient.getUser(event.requestId());
        UserDto mentorDto = userServiceClient.getUser(event.mentorId());
        return messageSource.getMessage(messageProperties.getPropertyMentorshipAccepted(),
                new Object[]{mentorDto.getUsername(), requesterDto.getUsername()}, locale);
    }
}
