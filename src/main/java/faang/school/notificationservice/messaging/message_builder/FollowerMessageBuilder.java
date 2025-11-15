package faang.school.notificationservice.messaging.message_builder;

import faang.school.notificationservice.dto.FollowerEventDto;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class FollowerMessageBuilder implements MessageBuilder<FollowerEventDto> {

    public FollowerMessageBuilder( ) {
    }

    @Override
    public Class<?> getInstance() {
        return FollowerEventDto.class;
    }

    @Override
    public String buildMessage(FollowerEventDto event, Locale locale) {
        return String.format("New follower: %s", event.getFollowerId());
    }

}
