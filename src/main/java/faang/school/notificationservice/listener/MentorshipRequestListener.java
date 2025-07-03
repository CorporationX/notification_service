package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.MentorshipRequestEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.SneakyThrows;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public abstract class MentorshipRequestListener extends AbstractEventListener<MentorshipRequestEvent> implements MessageListener {

    public MentorshipRequestListener(ObjectMapper objectMapper,
                                     List<MessageBuilder<MentorshipRequestEvent>> messageBuilders,
                                     UserServiceClient userServiceClient,
                                     List<NotificationService> notificationServices) {
        super(objectMapper, messageBuilders, userServiceClient, notificationServices);
    }

    @Override
    @SneakyThrows
    public void onMessage(Message message, byte[] pattern) {
        MentorshipRequestEvent event = objectMapper.readValue(message.getBody(), MentorshipRequestEvent.class);
        UserDto user = userServiceClient.getUser(event.getFoloweeId());
        String text = getMessage(event, user.getLocale());
        sendNotification(user.getId(), text);
    }
}