package faang.school.notificationservice.builder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class FollowerMessageBuilder implements MessageBuilder<FollowerEventDto>{
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;
    public String buildMessage(FollowerEventDto followerEventDto) {
        String followerName = userServiceClient.getUser(followerEventDto.getFollowerId()).getUsername();
        return messageSource.getMessage("follower.new", new Object[]{followerName}, LocaleContextHolder.getLocale());
    }
}
