package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.FollowerEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class FollowerEventMessageBuilder implements MessageBuilder<FollowerEventDto> {

    private final MessageSource messageSource;
    private final UserServiceClient serviceClient;

    @Override
    public String buildMessage(FollowerEventDto event, Locale locale) {
        UserDto user = serviceClient.getUser(event.followeeId());

        return messageSource.getMessage("follower.event", new Object[]{user.getUsername()}, locale);
    }
}
