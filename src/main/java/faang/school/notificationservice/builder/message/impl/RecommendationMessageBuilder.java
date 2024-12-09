package faang.school.notificationservice.builder.message.impl;

import faang.school.notificationservice.config.messageSource.MessageKeys;
import faang.school.notificationservice.message.event.RecommendationReceivedEvent;
import faang.school.notificationservice.builder.message.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RecommendationMessageBuilder implements MessageBuilder<RecommendationReceivedEvent> {

    private final MessageKeys messageKeys;
    private final MessageSource messageSource;

    @Override
    public String build(RecommendationReceivedEvent event, Locale locale) {
        return messageSource.getMessage(messageKeys.getRecommendationNew(), null, locale);
    }
}
