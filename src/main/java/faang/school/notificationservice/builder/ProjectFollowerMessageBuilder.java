package faang.school.notificationservice.builder;

import faang.school.notificationservice.dto.ProjectFollowerEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class ProjectFollowerMessageBuilder implements MessageBuilder<ProjectFollowerEvent> {
    private final MessageSource messageSource;

    @Autowired
    public ProjectFollowerMessageBuilder(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public Class<?> getInstance() {
        return ProjectFollowerEvent.class;
    }

    @Override
    public String buildMessage(ProjectFollowerEvent event, Locale locale, Object[] args) {
        return messageSource.getMessage("project_follower.new", args, locale);
    }
}
