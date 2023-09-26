package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.LikeEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LikeMessageBuilder implements MessageBuilder<LikeEventDto>{
    private final MessageSource messageSource;
    @Override
    public String buildMessage(LikeEventDto event, Locale locale) {
        return messageSource.getMessage("like.new", new Object[]{event.getCreatedAt()}, locale);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == LikeMessageBuilder.class;
    }
}
