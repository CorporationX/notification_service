package faang.school.notificationservice.service.recommendation;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.recommendation.RecommendationRequestEvent;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationRequestEventService recommendationRequestEventService;
    private final UserServiceClient userServiceClient;
    private final MessageBuilder<RecommendationRequestEvent> messageBuilder;

    @Override
    public void sendNotification(RecommendationRequestEvent recommendationRequestEvent) {
        UserDto receiverUser = userServiceClient.getUser(recommendationRequestEvent.getReceiverId());
        String messageToSend = messageBuilder.buildMessage(recommendationRequestEvent, Locale.getDefault());
        recommendationRequestEventService.apply(receiverUser, messageToSend);
    }
}