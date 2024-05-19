package faang.school.notificationservice.message;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.SkillOfferedEvent;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class SkillOfferedMessage implements MessageBuilder<SkillOfferedEvent> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public String buildMessage(SkillOfferedEvent eventType, Locale locale) {
        UserDto userDto = userServiceClient.getUser(eventType.getRecipientUserId());
        String defaultMessage = messageSource.getMessage("skill.offered", new Object[]{userDto.getUsername(), eventType.getTitleSkill()}, locale);
        return messageSource.getMessage("skill.offered", new Object[]{userDto.getUsername(), eventType.getTitleSkill()}, defaultMessage, locale);
    }

    @Override
    public Class<?> supportEventType() {
        return SkillOfferedEvent.class;
    }

    @Override
    public long getRequestAuthor(SkillOfferedEvent event) {
        return event.getSenderUserId();
    }
}
