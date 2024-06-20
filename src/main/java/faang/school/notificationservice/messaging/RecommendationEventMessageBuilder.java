package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.RecommendationRequestedEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RecommendationEventMessageBuilder implements MessageBuilder<RecommendationRequestedEventDto> {
    private final MessageSource messageSource;

    @Override
    public String buildMessage(RecommendationRequestedEventDto event, Locale locale) {
        Object[] args = {event.getAuthorId(), event.getCreatedAt()};
        return messageSource.getMessage("recommendation.new", args, locale);
    }
}
