package faang.school.notificationservice.listener.profile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.profile.ProfileViewEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class ProfileViewListener extends AbstractEventListener<ProfileViewEvent> {

    public ProfileViewListener(MessageBuilder<ProfileViewEvent> messageBuilder, List<NotificationService> notificationServices, UserServiceClient userServiceClient, ObjectMapper objectMapper) {
        super(messageBuilder, notificationServices, userServiceClient, objectMapper);
    }

    @KafkaListener(topics = "${spring.data.kafka.channels.comment-channel.name}", groupId = "${spring.data.kafka.group-id}")
    public void listen(String event) {
        try {
            ProfileViewEvent profileViewEvent = objectMapper.readValue(event, ProfileViewEvent.class);
            sendNotification(profileViewEvent.getUserId(), getMessage(profileViewEvent, Locale.ENGLISH));
        } catch (JsonProcessingException e) {
            throw new SerializationException(e);
        }
    }
}

