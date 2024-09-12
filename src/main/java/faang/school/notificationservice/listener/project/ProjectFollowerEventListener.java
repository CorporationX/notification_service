package faang.school.notificationservice.listener.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ProjectFollowerEvent;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ProjectFollowerEventListener extends AbstractEventListener<ProjectFollowerEvent> {
    private final UserServiceClient userServiceClient;

    public ProjectFollowerEventListener(List<NotificationService> notifiers,
                                        ObjectMapper mapper,
                                        MessageBuilder<ProjectFollowerEvent> messageBuilder,
                                        UserServiceClient userServiceClient) {
        super(notifiers, mapper, messageBuilder, ProjectFollowerEvent.class);
        this.userServiceClient = userServiceClient;
    }


    @Override
    public List<UserDto> getNotifiedUsers(ProjectFollowerEvent event) {
        UserDto notifiedUser = userServiceClient.getUser(event.getAuthorId());
        return List.of(notifiedUser);
    }

    @Override
    protected Object[] getArgs(ProjectFollowerEvent event) {
        return new Object[]{userServiceClient.getUser(event.getFollowerId()).getUsername()};
    }
}
