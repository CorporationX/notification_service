package faang.school.notificationservice.messaging.projectfollower;

import faang.school.notificationservice.event.projectfollower.ProjectFollowerEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ProjectFollowerMessageBuilder implements MessageBuilder<ProjectFollowerEvent> {

    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return ProjectFollowerEvent.class;
    }

    @Override
    public String buildMessage(ProjectFollowerEvent event, Locale locale) {
        Object[] args = { event.getProjectId(), event.getFollowerId() };

        return messageSource.getMessage("project.follower.new", args, locale);
    }
}
