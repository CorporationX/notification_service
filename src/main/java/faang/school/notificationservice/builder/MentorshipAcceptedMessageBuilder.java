package faang.school.notificationservice.builder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipAcceptedEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.MessageBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class MentorshipAcceptedMessageBuilder implements MessageBuilder<MentorshipAcceptedEvent> {

    private final UserServiceClient userServiceClient;

    private final MessageSource messageSource;

    @Override
    public String buildMessage(MentorshipAcceptedEvent event, Locale userLocale) {
        UserDto mentor = userServiceClient.getUser(event.getReceiverId());
        log.info("User mentor: {}, {}", mentor.getUsername(), mentor.getEmail());
        return messageSource.getMessage("mentorship_accepted", new Object[]{mentor.getUsername(), event.getRequestId()}, userLocale);
    }

    @Override
    public Class<?> supportsEventType() {
        return MentorshipAcceptedEvent.class;
    }
}
