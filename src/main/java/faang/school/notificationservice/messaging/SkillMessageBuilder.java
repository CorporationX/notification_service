package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.SkillDto;
import faang.school.notificationservice.event.SkillAcquiredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@RequiredArgsConstructor
@Component
public class SkillMessageBuilder implements MessageBuilder<SkillAcquiredEvent> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;
    @Override
    public Class<?> getInstance() {
        return SkillAcquiredEvent.class;
    }

    @Override
    public String buildMessage(SkillAcquiredEvent event, Locale locale) {
        SkillDto skillDto = userServiceClient.getSkill(event.getSkillId());
        return messageSource.getMessage("skill.new", new Object[]{skillDto.getTitle()}, locale);
    }
}
