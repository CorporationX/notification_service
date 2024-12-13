package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.like.LikeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LikeMessageBuilder implements MessageBuilder<LikeEvent>{

    @Value("like.new")
    private String likeKey;

    private final MessageSource messageSource;

    @Override
    public Class<LikeEvent> getInstance() {
        return LikeEvent.class;
    }

    @Override
    public String buildMessage(LikeEvent event, Locale locale) {
        return messageSource.getMessage(likeKey, null, locale);
    }
}
