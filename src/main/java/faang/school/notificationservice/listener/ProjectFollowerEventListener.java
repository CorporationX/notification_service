package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.NotificationData;
import faang.school.notificationservice.dto.ProjectFollowerEvent;
import faang.school.notificationservice.dto.UserNotificationDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class ProjectFollowerEventListener extends AbstractListener<ProjectFollowerEvent> implements MessageListener {
    public ProjectFollowerEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                        List<MessageBuilder<ProjectFollowerEvent>> messageBuilders,
                                        List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ProjectFollowerEvent projectFollowerEvent = getEvent(message, ProjectFollowerEvent.class);
        NotificationData notificationData = getNotificationData(projectFollowerEvent.getFollowerId());
        String textOfMessage = getMessage(projectFollowerEvent, Locale.UK, notificationData);
        UserNotificationDto userDto = userServiceClient.getDtoForNotification(projectFollowerEvent.getOwnerId());
        sendNotification(userDto, textOfMessage);
        log.info("sending notification for event {}", projectFollowerEvent);
    }
}