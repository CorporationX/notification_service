package faang.school.notificationservice.messaging.message_builder;

import faang.school.notificationservice.client.service.UserServiceClient;
import faang.school.notificationservice.dto.redis.RecommendationReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class RecommendationReceiveMessageBuilder extends AbstractMessageBuilder
        implements MessageBuilder<RecommendationReceivedEvent> {
    @Autowired
    public RecommendationReceiveMessageBuilder(UserServiceClient userServiceClient, MessageSource messageSource) {
        super(userServiceClient, messageSource);
    }

    @Override
    public String buildMessage(RecommendationReceivedEvent event, Locale locale) {
            String recommendationAuthorName = userServiceClient.getUser(event.getAuthorId()).getUsername();
            return messageSource.getMessage("recommendation.new", new String[]{recommendationAuthorName}, locale);
    }
}