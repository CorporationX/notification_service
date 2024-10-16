package faang.school.notificationservice.messaging.follower;

import faang.school.notificationservice.dto.follower.FollowerEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class FollowerMessageBuilder implements MessageBuilder<FollowerEventDto> {
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return FollowerEventDto.class;
    }

    @Override
    public String buildMessage(FollowerEventDto event, Locale locale) {
        Object[] args = {
                event.getUsername(),
                event.getFollowerId(),
                event.getFolloweeId()
        };
        return messageSource.getMessage("follower.new", args, locale);
    }
}
