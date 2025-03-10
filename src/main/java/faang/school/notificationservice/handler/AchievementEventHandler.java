package faang.school.notificationservice.handler;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.AchievementEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementEventHandler {

    private final UserServiceClient userServiceClient;
    private final MessageBuilder<AchievementEvent> messageBuilder;
    private final List<NotificationService> notificationService;

    @EventListener
    @Async
    @Retryable(retryFor = Exception.class, backoff = @Backoff(delay = 20000, multiplier = 2))
    public void handleAchievementEvent(AchievementEvent event) {
        log.info("Handling achievement event: {}", event);

        UserDto user = userServiceClient.getUser(event.getUserId());
        log.info("User found: {}", user);
        String message = messageBuilder.buildMessage(event, Locale.getDefault());
        notificationService.parallelStream().forEach(n -> n.send(user, message));
    }
}
