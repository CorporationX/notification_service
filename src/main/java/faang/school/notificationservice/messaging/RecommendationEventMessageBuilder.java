package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.RecommendationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RecommendationEventMessageBuilder implements MessageBuilder<RecommendationDto> {
    private final MessageSource messageSource;

    @Override
    public String buildMessage(RecommendationDto event, Locale locale) {
        Object[] args = {event.getAuthorId(), event.getCreatedAt()};
        return messageSource.getMessage("recommendation.new", args, locale);
    }
}
