package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.LikePostEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LikeEventMessageBuilder implements MessageBuilder<LikePostEvent>{
    private final MessageSource messageSource;
    @Override
    public Class<?> getInstance() {
        return LikePostEvent.class;
    }

    @Override
    public String buildMessage(LikePostEvent event, Locale locale) {
        return messageSource.getMessage("postlike.new ", null, locale);
    }
}
