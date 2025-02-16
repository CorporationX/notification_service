package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.RecommendationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RecommendationEventMessageBuilder implements MessageBuilder<RecommendationEvent> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public Class<?> getInstance() {
        return RecommendationEvent.class;
    }

    @Override
    public String buildMessage(RecommendationEvent event, Locale locale) {
        UserDto user = userServiceClient.getUser(event.requesterId());
        return messageSource.getMessage("You received a recommendation request from ",
                new Object[]{user.getUsername()}, locale);
    }
}
