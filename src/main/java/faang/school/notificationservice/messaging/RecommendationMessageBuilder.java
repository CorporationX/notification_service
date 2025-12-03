package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.recommendation.RecommendationEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@RequiredArgsConstructor
@Component
public class RecommendationMessageBuilder implements MessageBuilder<RecommendationEventDto> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public String buildMessage(RecommendationEventDto event, Locale locale) {
        String authorName = userServiceClient.getUser(event.authorId()).getUsername();
        String receiverName = userServiceClient.getUser(event.receiverId()).getUsername();
        String content = event.content();
        return messageSource.getMessage(
                "recommendation.received",
                new Object[]{receiverName, authorName, content},
                locale
        );
    }

    @Override
    public Class<RecommendationEventDto> getInstance() {
        return RecommendationEventDto.class;
    }
}