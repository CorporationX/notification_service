package faang.school.notificationservice.messaging;

import faang.school.notificationservice.model.event.RecommendationReceivedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RecommendationReceivedMessageBuilder implements MessageBuilder<RecommendationReceivedEvent> {
    private final MessageSource messageSource;

    @Override
    public String buildMessage(RecommendationReceivedEvent event, Locale locale) {
        var args = new Object[]{event.authorId()};
        return messageSource.getMessage("recommendation.received", args, locale);
    }
}
