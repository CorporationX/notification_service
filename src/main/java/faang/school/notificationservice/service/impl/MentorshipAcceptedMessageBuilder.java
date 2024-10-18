package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.feign.UserServiceClient;
import faang.school.notificationservice.model.event.MentorshipAcceptedEvent;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.service.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipAcceptedMessageBuilder implements MessageBuilder<MentorshipAcceptedEvent> {
    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Override
    public Class<MentorshipAcceptedEvent> getSupportedClass() {
        return MentorshipAcceptedEvent.class;
    }

    @Override
    public String buildMessage(MentorshipAcceptedEvent event, Locale locale) {
        UserDto requesterDto = userServiceClient.getUser(event.getRequesterId());
        UserDto mentorDto = userServiceClient.getUser(event.getMentorId());
        return messageSource.getMessage("mentorship.accepted",
                new Object[]{requesterDto.getUsername(), event.getRequestId(), mentorDto.getUsername()}, locale);
    }
}
