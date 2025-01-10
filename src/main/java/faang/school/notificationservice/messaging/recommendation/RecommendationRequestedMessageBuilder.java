package faang.school.notificationservice.messaging.recommendation;

import faang.school.notificationservice.dto.recommendation.RecommendationRequestedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RecommendationRequestedMessageBuilder implements MessageBuilder<RecommendationRequestedEvent> {
    private final MessageSource messageSource;

    @Value("recommendation-request.new")
    private String messageKey;

    @Override
    public Class<RecommendationRequestedEvent> getInstance() {
        return RecommendationRequestedEvent.class;
    }

    @Override
    public String buildMessage(RecommendationRequestedEvent event, Locale locale) {
        return messageSource.getMessage(messageKey, null, locale);
    }
}
