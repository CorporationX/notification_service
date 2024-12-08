package faang.school.notificationservice.listener.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.MentorshipAcceptedEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class MentorshipAcceptedEventListener extends AbstractEventListener<MentorshipAcceptedEvent> {

    @Value("${spring.data.redis.channel.mentorship.accepted}")
    private String mentorshipAcceptedChannel;

    public MentorshipAcceptedEventListener(
            ObjectMapper objectMapper,
            UserServiceClient userServiceClient,
            List<NotificationService> notificationService,
            List<MessageBuilder<MentorshipAcceptedEvent>> messageBuilder) {
        super(objectMapper, userServiceClient, notificationService, messageBuilder);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        processEvent(message, MentorshipAcceptedEvent.class, event -> {
            String messageText = getMessage(event, Locale.UK);
            sendNotification(event.getRequesterId(), messageText);
        });
    }

    @Override
    public MessageListenerAdapter getAdapter() {
        return new MessageListenerAdapter(this);
    }

    @Override
    public Topic getTopic() {
        return new ChannelTopic(mentorshipAcceptedChannel);
    }
}
