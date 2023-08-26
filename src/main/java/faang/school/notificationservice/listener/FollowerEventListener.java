package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.mapper.JsonObjectMapper;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEventListener> implements MessageListener {

    public FollowerEventListener(JsonObjectMapper jsonObjectMapper,
                                 UserServiceClient userServiceClient,
                                 List<MessageBuilder<FollowerEventListener>> followEventMessageBuilder,
                                 List<NotificationService> notificationServiceList) {
        super(jsonObjectMapper, userServiceClient, followEventMessageBuilder, notificationServiceList);
    }

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
