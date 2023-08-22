package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.messageBuilder.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultEventListener<T, V> extends AbstractEventListener<T, V> {
    public DefaultEventListener(UserServiceClient userServiceClient, List<NotificationService> notificationServices, List<MessageBuilder<T, V>> messageBuilders) {
        super(userServiceClient, notificationServices, messageBuilders);
    }
}
