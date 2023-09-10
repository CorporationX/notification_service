package faang.school.notificationservice.messageBuilder;

import faang.school.notificationservice.dto.redis.LikeEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LikeMessageBuilder implements MessageBuilder<LikeEventDto, String> {
    private final MessageSource messageSource;

    public String getPredefinedMessage(LikeEventDto event, Locale locale, String argument) {
        return messageSource.getMessage("like.new", new Object[]{event.getLikeAuthorName(), argument}, locale);
    }

    @Override
    public String buildMessage(LikeEventDto event, Locale locale, String argument) {
        return getPredefinedMessage(event,locale, argument);
    }

    @Override
    public Class<?> getEventType() {
        return LikeEventDto.class;
    }
}
