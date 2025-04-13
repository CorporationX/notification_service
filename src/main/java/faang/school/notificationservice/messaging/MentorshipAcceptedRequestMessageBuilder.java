package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipAcceptedRequestEvent;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipAcceptedRequestMessageBuilder implements MessageBuilder<MentorshipAcceptedRequestEvent> {
    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return MentorshipAcceptedRequestEvent.class;
    }

    @Override
    public String buildMessage(MentorshipAcceptedRequestEvent event, Locale locale) {
        UserDto mentor = userServiceClient.getUser(event.receiverId());
        return messageSource.getMessage("mentorship.accepted.notification",
                new Object[]{event.id(), mentor.getUsername()}, locale);
    }

}
