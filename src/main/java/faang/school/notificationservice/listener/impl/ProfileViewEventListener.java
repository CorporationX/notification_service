package faang.school.notificationservice.listener.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ProfileViewEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEvent> implements MessageListener {

    @Value("${spring.data.redis.channel.profile-view}")
    private String channelName;

    public ProfileViewEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                    List<NotificationService> notificationService,
                                    List<MessageBuilder<ProfileViewEvent>> messageBuilder) {
        super(objectMapper, userServiceClient, notificationService, messageBuilder);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        processEvent(message, ProfileViewEvent.class, event -> {
            String text = getMessage(event, Locale.UK);
            sendNotification(event.getProfileId(), text);
        });
    }

    @Override
    public Topic getTopic() {
        return new ChannelTopic(channelName);
    }

}
