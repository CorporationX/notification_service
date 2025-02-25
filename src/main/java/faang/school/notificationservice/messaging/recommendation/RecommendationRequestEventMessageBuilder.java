package faang.school.notificationservice.messaging.recommendation;

import faang.school.notificationservice.dto.recommendation.RecommendationRequestEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RecommendationRequestEventMessageBuilder implements MessageBuilder<RecommendationRequestEvent> {

    private static final String MESSAGE_KEY = "recommendation.request";

    private final MessageSource messageSource;

    @Override
    public String buildMessage(RecommendationRequestEvent event, Locale locale) {
        Object[] args = {event.getRequestId(), event.getAuthorId()};
        return messageSource.getMessage(MESSAGE_KEY, args, locale);
    }
}
