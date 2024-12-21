package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.event.AchievementEvent;
import faang.school.notificationservice.event.EventHandler;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AchievementEventListener extends AbstractListener<AchievementEvent> {
    private final UserServiceClient userServiceClient;

    public AchievementEventListener(ObjectMapper objectMapper,
                                    List<EventHandler<AchievementEvent>> eventHandlers,
                                    List<NotificationService> notificationServices,
                                    MessageBuilder<AchievementEvent> messageBuilder,
                                    UserServiceClient userServiceClient) {
        super(objectMapper, eventHandlers, notificationServices, messageBuilder);
        this.userServiceClient = userServiceClient;
    }


    @Override
    protected void handleEvent(AchievementEvent event) {
        UserContactsDto receiverDto = getUserContacts(event.getUserId());
        String message = createMessage(event);
        sendNotification(receiverDto, event, message);
    }

    @Retryable(retryFor = Exception.class,
            maxAttemptsExpression = "#{@retryProperties.maxAttempts}",
            backoff = @Backoff(
                    delayExpression = "#{@retryProperties.initialDelay}",
                    multiplierExpression = "#{@retryProperties.multiplier}",
                    maxDelayExpression = "#{@retryProperties.maxDelay}"
            )
    )
    public UserContactsDto getUserContacts(Long userId) {
        try {
            return userServiceClient.getUserContacts(userId);
        } catch (FeignException e) {
            log.error("Error occurred while fetching user contacts for user {}", userId, e);
            throw e;
        }
    }

    @Override
    protected Class<AchievementEvent> getEventType() {
        return AchievementEvent.class;
    }
}
