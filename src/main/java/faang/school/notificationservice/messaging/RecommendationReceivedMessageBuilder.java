package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.RecommendationReceivedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RecommendationReceivedMessageBuilder implements MessageBuilder<RecommendationReceivedEvent> {
    private final MessageSource source;

    @Override
    public Class<?> getInstance() {
        return RecommendationReceivedEvent.class;
    }

    @Override
    public String buildMessage(RecommendationReceivedEvent event, Locale locale) {
        return source.getMessage("recommendation_received.new", new Object[] {"${authorName}"}, locale);
    }
}
