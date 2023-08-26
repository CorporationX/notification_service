package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.FollowerEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class FollowEventMessageBuilder implements MessageBuilder<FollowerEventDto> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(FollowerEventDto event, Locale locale) {
        return messageSource.getMessage("follower.new", new Object[] {userName}, locale);
    }

    @Override
    public Class<?> supportsEventType() {
        return null;
    }
}
