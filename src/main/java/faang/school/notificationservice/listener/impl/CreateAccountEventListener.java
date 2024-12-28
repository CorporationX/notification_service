package faang.school.notificationservice.listener.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.ProjectServiceClient;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ProjectDto;
import faang.school.notificationservice.event.account.CreateAccountEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class CreateAccountEventListener extends AbstractEventListener<CreateAccountEvent> {

    @Value("${spring.data.redis.channel.create-account}")
    private String topic;

    private final ProjectServiceClient projectServiceClient;

    public CreateAccountEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                      List<NotificationService> notificationService,
                                      List<MessageBuilder<CreateAccountEvent>> messageBuilder, ProjectServiceClient projectServiceClient) {
        super(objectMapper, userServiceClient, notificationService, messageBuilder);
        this.projectServiceClient = projectServiceClient;
    }

    @Override
    public ChannelTopic getTopic() {
        return new ChannelTopic(topic);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        processEvent(message, CreateAccountEvent.class, event -> {
            String text = getMessage(event, Locale.UK);

            if (event.getOwnerType().equals("USER")) {
                sendNotification(event.getOwnerId(), text);
            } else if (event.getOwnerType().equals("PROJECT")) {
                ProjectDto dto = projectServiceClient.getProject(event.getOwnerId());
                sendNotification(dto.getOwnerId(), text);
            }
        });
    }
}
