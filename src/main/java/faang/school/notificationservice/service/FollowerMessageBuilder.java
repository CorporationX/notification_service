package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class FollowerMessageBuilder implements MessageBuilder<FollowerEvent> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(UserDto follower, Locale locale) {
        Object[] args = {follower.getUsername()};
        return messageSource.getMessage("follower.new", args, locale);
    }

    @Override
    public FollowerEvent getEvent(FollowerEvent followerEvent) {
        return followerEvent;
    }
}