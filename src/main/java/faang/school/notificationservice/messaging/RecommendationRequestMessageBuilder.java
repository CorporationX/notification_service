package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.redis.RecommendationRequestEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RecommendationRequestMessageBuilder implements MessageBuilder<RecommendationRequestEvent> {

    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public Class<?> getInstance() {
        return RecommendationRequestEvent.class;
    }

    @Override
    public String buildMessage(RecommendationRequestEvent event, Locale locale) {
        UserDto user = userServiceClient.getUser(event.getRequesterId());
        return messageSource.getMessage("recommendation.receiver", new Object[]{user.getUsername()}, locale);
    }
}
