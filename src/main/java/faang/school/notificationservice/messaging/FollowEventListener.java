package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
public class FollowEventListener {
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notifications;
    private final MessageBuilder<FollowEventDto> messageBuilder;

    public void onMessage(FollowEventDto event) {
        long followeeId = event.followeeId();
        UserDto user = userServiceClient.getUser(followeeId);
        if (user.getPreference() == null) {
            log.info("User did not specify a preferred contact method. Notification was not sent.");
            return;
        }
        notifications.stream()
                .filter(n -> n.getPreferredContact() == user.getPreference())
                .findFirst()
                .ifPresentOrElse(
                        notification -> notification.send(user, messageBuilder.buildMessage(event, Locale.ENGLISH)),
                        () -> log.warn("No matching notification service found for user preference: {}", user.getPreference())
                );
    }

}
