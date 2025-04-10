package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Класс для построения сообщений о событиях подписки/отписки пользователей.
 * Реализует интерфейс MessageBuilder для создания сообщений на основе событий.
 */
@Component
@RequiredArgsConstructor
public class FollowerEventMessageBuilder implements MessageBuilder<FollowerEvent> {

    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public Class<FollowerEvent> getInstance() {
        return FollowerEvent.class;
    }

    @Override
    public String buildMessage(FollowerEvent event, Locale locale) {
        UserDto follower = userServiceClient.getUser(event.getFollowerId());
        String followerName = follower != null ? follower.getUsername() : "Unknown";

        return messageSource.getMessage(
                "follower.new",
                new Object[]{followerName},
                locale
        );
    }
}
