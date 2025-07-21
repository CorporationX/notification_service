package faang.school.notificationservice.service.notification;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.notification.AggregatedNotificationsDto;
import faang.school.notificationservice.messaging.like.CommentLikedEventMessageBuilder;
import faang.school.notificationservice.messaging.like.PostLikedEventMessageBuilder;
import faang.school.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationSenderService {

    private final List<NotificationService> notificationServices;
    private final CommentLikedEventMessageBuilder commentMessageBuilder;
    private final PostLikedEventMessageBuilder postMessageBuilder;
    private final UserServiceClient userServiceClient;
    private final NotificationRepository notificationRepository;

    public void send(UserDto userDto, String message) {
        notificationServices.stream()
                .filter(notificationService ->
                        notificationService.getPreferredContact() == userDto.getPreference())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No notification service was found for user preferred notification type"))
                .send(userDto, message);
    }

    public void sendAggregatedNotifications(AggregatedNotificationsDto notification) {
        EventType eventType = notification.getEventType();

        try {
            sendMessageByType(notification, eventType);

            notificationRepository.updateStatusByGroup(
                    notification.getReceiverId(),
                    notification.getTargetEntityId(),
                    eventType.name(),
                    NotificationStatus.SENT.name()
            );
        } catch (RuntimeException e) {
            log.error(
                    "Failed to send notification for user {}, entityId: {}, eventType: {}",
                    notification.getReceiverId(),
                    notification.getTargetEntityId(),
                    eventType.name(),
                    e
            );

            notificationRepository.updateStatusByGroup(
                    notification.getReceiverId(),
                    notification.getTargetEntityId(),
                    eventType.name(),
                    NotificationStatus.FAILED.name()
            );
        }
    }

    private void sendMessageByType(AggregatedNotificationsDto notification, EventType eventType) {
        UserDto user = userServiceClient.getUser(notification.getReceiverId());
        Locale locale = getLocale(user);

        switch (eventType) {
            case COMMENT_LIKED -> send(
                    user, commentMessageBuilder.buildMessage(notification, locale)
            );
            case POST_LIKED -> send(
                    user, postMessageBuilder.buildMessage(notification, locale)
            );
        }
    }

    private Locale getLocale(UserDto user) {
        return user.getLocale() == null ? Locale.getDefault() : user.getLocale();
    }
}