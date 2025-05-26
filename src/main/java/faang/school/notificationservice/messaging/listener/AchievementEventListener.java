package faang.school.notificationservice.messaging.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.AchievementEventDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class AchievementEventListener<T> {
    /*private final UserServiceClient userServiceClient;
    private final List<NotificationService> notifications;*/


  /*  public void onMessage2(AchievementEventDto event) {
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
    }*/

}
