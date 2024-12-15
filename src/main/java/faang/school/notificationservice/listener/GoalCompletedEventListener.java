package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.event.GoalCompletedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class GoalCompletedEventListener extends AbstractListener<GoalCompletedEvent> {
    private final UserServiceClient userServiceClient;
    private final MessageBuilder<GoalCompletedEvent> messageBuilder;
    private final List<NotificationService> notificationServices;

    public GoalCompletedEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                      MessageBuilder<GoalCompletedEvent> messageBuilder,
                                      List<NotificationService> notificationServices) {
        super(objectMapper, GoalCompletedEvent.class);
        this.userServiceClient = userServiceClient;
        this.messageBuilder = messageBuilder;
        this.notificationServices = notificationServices;
    }

    @Override
    protected void handleEvent(GoalCompletedEvent event) {
        UserContactsDto user = getUserContacts(event.getActorId());
        sendNotification(user, event);
    }

    @Retryable(retryFor = Exception.class,
            maxAttemptsExpression = "#{@retryProperties.maxAttempts}",
            backoff = @Backoff(
                    delayExpression = "#{@retryProperties.initialDelay}",
                    multiplierExpression = "#{@retryProperties.multiplier}",
                    maxDelayExpression = "#{@retryProperties.maxDelay}"
            )
    )
    private UserContactsDto getUserContacts(Long userId) {
        try {
            return userServiceClient.getUserContacts(userId);
        } catch (FeignException e) {
            log.error("Error occurred while fetching user contacts for user {}", userId, e);
            throw e;
        }
    }

    private void sendNotification(UserContactsDto user, GoalCompletedEvent event) {
        String message = messageBuilder.buildMessage(event, LocaleContextHolder.getLocale());
        notificationServices.stream()
                .filter(service -> user.getPreference() == service.getPreferredContact())
                .findFirst()
                .ifPresentOrElse(
                        service -> {
                            service.send(user, message);
                            log.info("Message sent to user {} via {}", user.getId(), user.getPreference());
                        },
                        () -> log.warn("No notification service found for user preference: {}. Message not sent.", user.getPreference())
                );
    }
}
