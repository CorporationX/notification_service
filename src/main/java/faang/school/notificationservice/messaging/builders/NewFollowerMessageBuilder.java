package faang.school.notificationservice.messaging.builders;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.events.NewFollowerEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class NewFollowerMessageBuilder implements MessageBuilder<NewFollowerEventDto> {

    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return NewFollowerEventDto.class;
    }

    @Override
    public String buildMessage(NewFollowerEventDto event, Locale locale) {
        return messageSource.getMessage(
                "follower.new.notification.message",
                new Object[]{event.followerDisplayName()},
                locale
        );
    }
}