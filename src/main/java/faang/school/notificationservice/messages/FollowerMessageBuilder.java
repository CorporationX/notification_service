package faang.school.notificationservice.messages;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Locale;


@Component
@RequiredArgsConstructor
public class FollowerMessageBuilder<T> implements MessageBuilder<FollowerEventDto>{

    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Override
    public String buildMessage(FollowerEventDto event, Locale locale) {
        String followerName = userServiceClient.getUserInternal(event.getFollowerId()).username();

        return messageSource.getMessage("follower.new", new Object[]{followerName}, locale);
    }

    @Override
    public boolean supportsEventType(FollowerEventDto eventType) {
        return eventType.getClass() == FollowerEventDto.class;
    }
}
