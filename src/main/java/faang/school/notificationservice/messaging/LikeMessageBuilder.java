package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.MessageDto;
import faang.school.notificationservice.event.LikeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LikeMessageBuilder implements MessageBuilder<LikeEvent> {
    private final MessageSource messageSource;

    @Override
    public String buildMessage(LikeEvent event, MessageDto messageDto, Locale locale) {
        return messageSource.getMessage("post.like.new", new Object[]{messageDto.getAuthorName(),
                messageDto.getLikeAuthorName(),
                messageDto.getPostName()}, locale);
    }

    @Override
    public Class<?> supportsEventType() {
        return LikeEvent.class;
    }


}
