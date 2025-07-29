package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.SkillAcquiredEvent;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class SkillAcquiredMessageBuilder implements MessageBuilder<SkillAcquiredEvent> {

    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public Class<?> getInstance() {
        return SkillAcquiredEvent.class;
    }

    @Override
    public String buildMessage(SkillAcquiredEvent event, Locale locale) {
        UserDto user = userServiceClient.getUser(event.getUserId());
        return messageSource.getMessage(
                "skill.acquired",
                new Object[]{user.getUsername(), event.getSkillId()},
                locale
        );
    }
}
