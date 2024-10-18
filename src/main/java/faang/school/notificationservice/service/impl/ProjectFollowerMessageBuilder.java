package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.feign.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.ProjectFollowerEvent;
import faang.school.notificationservice.service.MessageBuilder;
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
    public Class<ProjectFollowerEvent> getSupportedClass() {
        return ProjectFollowerEvent.class;
    }

    @Override
    public String buildMessage(ProjectFollowerEvent event, Locale locale) {
        UserDto projectFollowerDto = userServiceClient.getUser(event.getFollowerId());
        UserDto projectCreatorDto = userServiceClient.getUser(event.getCreatorId());
        return messageSource.getMessage("new.project.follower",
                new Object[]{projectCreatorDto.getUsername(), projectFollowerDto.getUsername(), event.getProjectId()},
                locale);
    }
}