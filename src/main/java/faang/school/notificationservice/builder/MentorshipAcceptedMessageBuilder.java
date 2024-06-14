package faang.school.notificationservice.builder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipAcceptedEvent;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class MentorshipAcceptedMessageBuilder implements MessageBuilder<MentorshipAcceptedEvent> {

    private final UserServiceClient userServiceClient;

    private final MessageSource messageSource;

    @Override
    public String buildMessage(MentorshipAcceptedEvent event) {
        UserDto mentee = userServiceClient.getUser(event.getActorId());
        UserDto mentor = userServiceClient.getUser(event.getReceiverId());
        Locale userLocale = Arrays.stream(Locale.getAvailableLocales())
                .filter(locale -> locale.getDisplayName().toUpperCase().equals(mentee.getPreferredLocale()))
                        .findFirst().orElse(null);
        log.info("User mentor: {}, {}", mentor.getUsername(), mentor.getEmail());
        return messageSource.getMessage("mentorship_accepted", new Object[]{mentor.getUsername(), event.getRequestId()}, userLocale);
    }

    @Override
    public Class<?> supportsEventType() {
        return MentorshipAcceptedEvent.class;
    }
}
