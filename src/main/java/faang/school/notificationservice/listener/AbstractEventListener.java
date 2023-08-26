package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.mapper.JsonObjectMapper;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    private final JsonObjectMapper jsonObjectMapper;
    private final UserServiceClient userServiceClient;
    private final List<MessageBuilder<T>> followEventMessageBuilder;
    private final List<NotificationService> notificationServiceList;


}
