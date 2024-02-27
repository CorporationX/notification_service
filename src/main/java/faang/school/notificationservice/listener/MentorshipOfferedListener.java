package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.context.UserContext;
import faang.school.notificationservice.dto.MentorshipRequestDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class MentorshipOfferedListener extends AbstractEventListener<MentorshipRequestDto> implements MessageListener {

    @Autowired
    public MentorshipOfferedListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                     List<MessageBuilder<MentorshipRequestDto>> messageBuilders,
                                     List<NotificationService> notificationServices, UserContext userContext) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices, userContext);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, MentorshipRequestDto.class, event -> {
            userContext.setUserId(event.getReceiver());
            UserDto receiver = userServiceClient.getUser(event.getReceiver());
            String text = getMessage(event, Locale.UK);
            sendNotification(receiver, text);
        });
    }
}
