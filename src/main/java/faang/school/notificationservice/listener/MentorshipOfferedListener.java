package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipOfferedEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class MentorshipOfferedListener extends AbstractEventListener<MentorshipOfferedEventDto> implements MessageListener {

    public MentorshipOfferedListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                     List<MessageBuilder<MentorshipOfferedEventDto>> messageBuilders,
                                     List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, MentorshipOfferedEventDto.class, event -> {
            UserDto requester = userServiceClient.getUser(event.getRequesterId());
            String text = getMessage(event, Locale.UK);
            sendNotification(event.getReceiverId(), text);
        });
    }
}
