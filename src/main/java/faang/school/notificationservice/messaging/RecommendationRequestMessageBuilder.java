package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.RecommendationRequestEvent;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class RecommendationRequestMessageBuilder implements MessageBuilder<RecommendationRequestEvent>{
    private MessageSource messageSource;

    @Override
    public String buildMessage(RecommendationRequestEvent event, Locale locale) {
        return messageSource.getMessage("recommendationRequest", new Object[]{event.requesterId()}, locale);
    }

    @Override
    public Class<?> supportsEventType() {
        return RecommendationRequestEvent.class;
    }
}
