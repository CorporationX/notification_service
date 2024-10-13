package faang.school.notificationservice.messaging.follower;

import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class FollowerMessageBuilder implements MessageBuilder<UserDto> {

    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return UserDto.class;
    }

    @Override
    public String buildMessage(UserDto follower, Locale locale) {
        Object[] args = {follower.getUsername()};

        return messageSource.getMessage("follower.new", args, locale);
    }
}