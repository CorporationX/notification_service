package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.RecommendationReceivedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RecommendationMessageBuilder implements MessageBuilder<RecommendationReceivedEvent> {
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return RecommendationReceivedEvent.class;
    }

    @Override
    public String buildMessage(RecommendationReceivedEvent event, Locale locale) {
        Object[] placeholders = {event.getReceiverName(), event.getAuthorName()};
        return messageSource.getMessage("recommendation.new", placeholders, locale);
    }
}
