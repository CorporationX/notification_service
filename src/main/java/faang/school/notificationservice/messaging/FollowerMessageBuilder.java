package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

import java.util.Locale;

@RequiredArgsConstructor
public class FollowerMessageBuilder implements MessageBuilder<FollowerDto> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public Class<?> getInstance() {
        return FollowerDto.class;
    }

    @Override
    public String buildMessage(FollowerDto event, Locale locale) {
        return messageSource.getMessage(
                "follower.new",
                new Object[]{userServiceClient.getUser(event.getFollowerId()).getUsername()}, locale
        );
    }
}
