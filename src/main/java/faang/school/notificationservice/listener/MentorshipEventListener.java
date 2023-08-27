package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messageBuilder.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MentorshipEventListener extends AbstractEventListener<MentorshipEventDto> implements MessageListener {

    public MentorshipEventListener(ObjectMapper objectMapper,
                                   UserServiceClient userServiceClient,
                                   List<MessageBuilder<MentorshipEventDto>> messageBuilders,
                                   List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, MentorshipEventDto.class, event -> {
            UserDto requester = userServiceClient.getUser(event.getRequesterId());
            String text = getMessage(event, requester.getLocale());
            sendNotification(event.getRequesterId(), text);
        });

    }
}
