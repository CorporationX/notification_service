package faang.school.notificationservice.builder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationReceivedEvent;
import faang.school.notificationservice.model.EventType;
import faang.school.notificationservice.service.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@RequiredArgsConstructor
@Component
public class RecommendationReceiveMessageBuilder implements MessageBuilder<RecommendationReceivedEvent> {

        private final UserServiceClient userServiceClient;
        private final MessageSource messageSource;

        @Override
        public String buildMessage(RecommendationReceivedEvent event, Locale locale) {
                String recommendationAuthorName = userServiceClient.getUser(event.getAuthorId()).getUsername();
                return messageSource.getMessage("recommendation.new", new String[]{recommendationAuthorName}, locale);
        }

        @Override
        public boolean supportsEventType(RecommendationReceivedEvent event) {
                return event.getEventType().equals(EventType.RECOMMENDATION_RECEIVED);
        }
}