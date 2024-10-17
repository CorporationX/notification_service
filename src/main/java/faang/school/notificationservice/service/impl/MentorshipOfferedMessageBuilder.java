package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.MentorshipOfferedEvent;
import faang.school.notificationservice.model.event.ProjectFollowerEvent;
import faang.school.notificationservice.service.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipOfferedMessageBuilder implements MessageBuilder<MentorshipOfferedEvent> {
    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Override
    public Class<MentorshipOfferedEvent> getSupportedClass() {
        return MentorshipOfferedEvent.class;
    }

    @Override
    public String buildMessage(MentorshipOfferedEvent event, Locale locale) {
        UserDto requesterDto = userServiceClient.getUser(event.getRequesterId());
        UserDto mentorDto = userServiceClient.getUser(event.getMentorId());
        return messageSource.getMessage("mentorship.offered",
                new Object[]{mentorDto.getUsername(), requesterDto.getUsername()},
                locale);
    }
}