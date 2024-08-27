package faang.school.notificationservice.messaging.mentorship;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.mentorship.request.MentorshipOfferedEvent;
import faang.school.notificationservice.exception.ExceptionMessages;
import faang.school.notificationservice.exception.feign.UserServiceException;
import faang.school.notificationservice.messaging.MessageBuilder;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipOfferedMessageBuilder implements MessageBuilder<MentorshipOfferedEvent> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public Class<MentorshipOfferedEvent> getInstance() {
        return MentorshipOfferedEvent.class;
    }

    @Override
    public String buildMessage(MentorshipOfferedEvent event, Locale locale) {
        var receiver = fetchUser(event.getRequesterId());
        return messageSource.getMessage("mentorship.offered",
                new Object[]{receiver.getUsername()}, locale);
    }

    private UserDto fetchUser(Long id) {
        UserDto user;
        try {
            user = userServiceClient.getUser(id);
        } catch (FeignException | RestClientException exception) {
            throw new UserServiceException(String.format(ExceptionMessages.USER_DATA_FETCH_FAILURE, id), exception);
        }
        return user;
    }
}
