package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.mapper.JsonObjectMapper;
import faang.school.notificationservice.messageBuilder.FollowEventMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FollowerEventListener extends AbstractEventListener {

    private final JsonObjectMapper jsonObjectMapper;
    private final List<NotificationService> notificationServiceList;
    private final UserServiceClient userServiceClient;
    private final FollowEventMessageBuilder followEventMessageBuilder;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        FollowerEventDto followerEventDto = jsonObjectMapper.readValue(message.getBody(), FollowerEventDto.class);
        UserDto user = userServiceClient.getUser(followerEventDto.getFollowerId());
        String notificationText = followEventMessageBuilder.buildMessage(user.getLocale(), user.getUsername());
        notificationServiceList.stream()
                .filter((service) -> service.getPreferredContact() == user.getPreference())
                .forEach(notificationService -> notificationService.send(user, notificationText));
    }
}
