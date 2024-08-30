package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ProjectFollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ProjectFollowerEventListener extends AbstractEventListener<ProjectFollowerEvent>{
    public ProjectFollowerEventListener(ObjectMapper objectMapper,
                                        UserServiceClient userServiceClient,
                                        List<NotificationService> notificationServiceList,
                                        List<MessageBuilder<ProjectFollowerEvent>> messageBuilders,
                                        MessageBuilder<ProjectFollowerEvent> messageBuilder) {
        super(objectMapper, userServiceClient, notificationServiceList, messageBuilders, ProjectFollowerEvent.class, messageBuilder);
    }

    @Override
    protected List<UserDto> getNotifiedUsers(ProjectFollowerEvent event) {
        UserDto notifiedUser = userServiceClient.getUser(event.getAuthorId());
        return List.of(notifiedUser);
    }

    @Override
    protected Object[] getArgs(ProjectFollowerEvent event) {
        return new Object[]{userServiceClient.getUser(event.getFollowerId()).getUsername()};
    }
}
