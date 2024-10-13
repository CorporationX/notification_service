package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.ProjectFollowerEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ProjectFollowerMessageBuilder implements MessageBuilder<ProjectFollowerEvent> {
    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Override
    public Class<ProjectFollowerEvent> getInstance() {
        return ProjectFollowerEvent.class;
    }

    @Override
    public String buildMessage(ProjectFollowerEvent event, Locale locale) {
        UserDto projectFollowerDto = userServiceClient.getUser(event.getFollowerId());
        UserDto projectCreatorDto = userServiceClient.getUser(event.getCreatorId());
        return messageSource.getMessage("new_project_follower",
                new Object[]{projectCreatorDto.getUsername(), projectFollowerDto.getUsername(), event.getProjectId()}, locale);
    }
}