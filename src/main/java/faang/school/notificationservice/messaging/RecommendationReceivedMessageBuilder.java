package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationReceivedEvent;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RecommendationReceivedMessageBuilder implements MessageBuilder<RecommendationReceivedEvent> {

    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return RecommendationReceivedEvent.class;
    }

    @Override
    public String buildMessage(RecommendationReceivedEvent event, Locale locale) {
        UserDto author = userServiceClient.getUser(event.authorId());
        return messageSource.getMessage("recommendation.received.notification",
                new Object[]{author.getUsername(), event.message()}, locale);
    }
}
