package faang.school.notificationservice.service.notification.implimentation;

import faang.school.notificationservice.event.kafka.CommentLikedNotificationEvent;
import faang.school.notificationservice.model.PendingNotifications;
import faang.school.notificationservice.repository.NotificationRepository;
import faang.school.notificationservice.service.notification.NotificationEventHandler;
import faang.school.notificationservice.service.notification.NotificationStatus;
import faang.school.notificationservice.service.notification.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentLikedNotificationEventHandler implements NotificationEventHandler<CommentLikedNotificationEvent> {

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public void handle(CommentLikedNotificationEvent event) {
        PendingNotifications notification = PendingNotifications.builder()
                .recipientId(event.getOwner().getId())
                .notificationType(NotificationType.COMMENT_LIKED.name())
                .status(NotificationStatus.PENDING.name())
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }
}
