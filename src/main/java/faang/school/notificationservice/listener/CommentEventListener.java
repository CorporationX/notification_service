package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEvent;
import faang.school.notificationservice.messaging.CommentEventMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentEventListener extends AbstractListener<CommentEvent> {

    public CommentEventListener(ObjectMapper objectMapper,
                                UserServiceClient userServiceClient,
                                List<NotificationService> notificationServices,
                                CommentEventMessageBuilder commentEventMessageBuilder) {
        super(objectMapper, userServiceClient, notificationServices, commentEventMessageBuilder);
    }


}
