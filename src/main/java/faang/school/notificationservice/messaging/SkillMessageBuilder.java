package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.SkillDto;
import faang.school.notificationservice.event.SkillAcquiredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@RequiredArgsConstructor
@Component
@Slf4j
public class SkillMessageBuilder implements MessageBuilder<SkillAcquiredEvent> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public Class<?> getInstance() {
        return SkillAcquiredEvent.class;
    }

    @Override
    public String buildMessage(SkillAcquiredEvent event, Locale locale) {
        log.info("Validate locale for null");
        if (locale == null) {
            locale = Locale.UK;
        }
        log.info("getting skillDto from userService, using user service client");
        SkillDto skillDto = userServiceClient.getSkill(event.getSkillId());

        log.info("get message from message properties");
        return messageSource.getMessage("skill.new", new Object[]{skillDto.getTitle()}, locale);
    }
}
