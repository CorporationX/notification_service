package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.like.LikeEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LikeEventMessageBuilder implements MessageBuilder<LikeEventDto> {

    private static final String MESSAGE_KEY = "notification.like";

    private final MessageSource messageSource;

    @Override
    public Class<LikeEventDto> getInstance() {
        return LikeEventDto.class;
    }

    @Override
    public String buildMessage(LikeEventDto event, Locale locale) {
        return messageSource.getMessage(
                MESSAGE_KEY,
                new Object[]{event.getLikerId(), event.getPostId()},
                locale
        );
    }
}
