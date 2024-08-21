package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.FollowerEvent;
import lombok.Data;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;


@Configuration
@Data
public class FollowerMessageBuilder implements MessageBuilder<FollowerEvent> {
    private MessageSource messageSource;
    private UserServiceClient userServiceClient;

    @Override
    public Class<?> getInstance() {
        return FollowerEvent.class;
    }

    @Override
    public String buildMessage(FollowerEvent event, Locale locale) {
        UserDto user = userServiceClient.getUser(event.getFollowerId());
        String userName = user.getUsername();
        return messageSource.getMessage("follower.new",
                new Object[]{userName},
                locale);
    }
}
