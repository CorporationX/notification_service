package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.RequestMentorshipEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipAcceptedMessageBuilder implements MessageBuilder<RequestMentorshipEvent> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public Class<?> getInstance() {
        return RequestMentorshipEvent.class;
    }

    @Override
    public String buildMessage(RequestMentorshipEvent event, Locale locale) {
        String mentor = userServiceClient.getUser(event.getMentorId()).getUsername();
        return messageSource.getMessage("mentorship.new", new Object[]{mentor}, locale);
    }
}
