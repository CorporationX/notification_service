package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.RecommendationRequestedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RecommendationRequestMessageBuilder implements MessageBuilder<RecommendationRequestedEvent> {
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return RecommendationRequestedEvent.class;
    }

    @Override
    public String buildMessage(RecommendationRequestedEvent event, Locale locale) {
        return "";
    }

    @Override
    public boolean isEventTypeSupported(Object event) {
        return event instanceof RecommendationRequestedEvent;
    }

    @Override
    public String buildMessage(RecommendationRequestedEvent event, Locale locale, Object... placeholders) {
        return messageSource.getMessage("recommendation.requested", placeholders, locale);
    }
}
