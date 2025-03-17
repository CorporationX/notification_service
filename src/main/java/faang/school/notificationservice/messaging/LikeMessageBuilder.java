package faang.school.notificationservice.messaging;

import faang.school.notificationservice.model.LikeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LikeMessageBuilder implements MessageBuilder<LikeEvent> {
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return LikeMessageBuilder.class;
    }

    @Override
    public String buildMessage(LikeEvent event, Locale locale) {
        return messageSource.getMessage("like.post",
                new Object[]{event.getLikerUsername(), event.getPostTitle()},
                locale);
    }
}
