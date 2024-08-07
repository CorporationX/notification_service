package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;

import java.util.List;

public class TestEventListener extends AbstractEventListener<Object> {
    public TestEventListener(ObjectMapper objectMapper,
                             UserServiceClient userServiceClient,
                             List<MessageBuilder<Object>> messageBuilders,
                             List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }
}