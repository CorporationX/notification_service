package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.feign.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.UserFollowerEvent;
import faang.school.notificationservice.service.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class UserFollowerMessageBuilder implements MessageBuilder<UserFollowerEvent> {
    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Override
    public Class<UserFollowerEvent> getSupportedClass() {
        return UserFollowerEvent.class;
    }

    @Override
    public String buildMessage(UserFollowerEvent event, Locale locale) {
        UserDto followerDto = userServiceClient.getUser(event.getFollowerId());
        UserDto followedUserDto = userServiceClient.getUser(event.getFollowedUserId());
        return messageSource.getMessage("new.follower",
                new Object[]{followedUserDto.getUsername(), followerDto.getUsername()},
                locale);
    }
}