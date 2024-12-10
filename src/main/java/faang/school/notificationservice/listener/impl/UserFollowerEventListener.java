package faang.school.notificationservice.listener.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.event.follower.UserFollowerEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class UserFollowerEventListener extends AbstractEventListener<UserFollowerEvent> {

    @Value("${spring.data.redis.channel.follower}")
    private String channelName;

    public UserFollowerEventListener(
            ObjectMapper objectMapper,
            UserServiceClient userServiceClient,
            List<NotificationService> notificationService,
            List<MessageBuilder<UserFollowerEvent>> messageBuilder
    ) {
        super(objectMapper, userServiceClient, notificationService, messageBuilder);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        processEvent(message, UserFollowerEvent.class, event -> {
            String messageText = getMessage(event, Locale.ENGLISH);
            sendNotification(event.getFolloweeId(), messageText);
        });
    }

    @Override
    public ChannelTopic getTopic() {
        return new ChannelTopic(channelName);
    }
}
