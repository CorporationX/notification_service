package faang.school.notificationservice.listener.projectfollower;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.event.projectfollower.ProjectFollowerEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

@Slf4j
@Component
public class ProjectFollowerMessageListener extends AbstractEventListener<ProjectFollowerEvent> {

    public ProjectFollowerMessageListener(
            ObjectMapper objectMapper,
            UserServiceClient userServiceClient,
            Map<Class<?>, MessageBuilder<?>> messageBuilders,
            Map<UserDto.PreferredContact, NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, ProjectFollowerEvent.class, event -> {
            log.info("A project with ID " + event.getProjectId() + " has been followed.");
            UserDto creatorUser = userServiceClient.getUser(event.getCreatorId());
            Locale userPreferredLocale = creatorUser.getLocale() != null ? creatorUser.getLocale() : Locale.ENGLISH;
            String text = getMessage(event, userPreferredLocale);

            sendNotification(creatorUser, text);
        });
    }
}
