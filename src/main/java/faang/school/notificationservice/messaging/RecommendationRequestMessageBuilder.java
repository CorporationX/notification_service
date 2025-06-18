package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.RecommendationRequestEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RecommendationRequestMessageBuilder implements MessageBuilder<RecommendationRequestEvent> {
    private final MessageSource messageSource;

    @Override
    public Class<RecommendationRequestEvent> supportsEventType() {
        return RecommendationRequestEvent.class;
    }

    @Override
    public String buildMessage(RecommendationRequestEvent event, Locale locale) {
        return messageSource.getMessage("recommendation.request",
                new Object[]{event.getAuthor().getUsername()}, locale);
    }
}
