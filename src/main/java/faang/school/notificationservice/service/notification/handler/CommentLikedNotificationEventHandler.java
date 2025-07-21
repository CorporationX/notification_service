package faang.school.notificationservice.service.notification.handler;

import faang.school.notificationservice.dto.notification.PendingNotificationsDto;
import faang.school.notificationservice.event.kafka.CommentLikedNotificationEvent;
import faang.school.notificationservice.mapper.PendingNotificationsMapper;
import faang.school.notificationservice.model.PendingNotifications;
import faang.school.notificationservice.repository.NotificationRepository;
import faang.school.notificationservice.service.notification.EventType;
import faang.school.notificationservice.service.notification.NotificationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikedNotificationEventHandler implements NotificationEventHandler<CommentLikedNotificationEvent> {

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public void saveNotification(CommentLikedNotificationEvent event) {
        PendingNotificationsDto notificationsDto = PendingNotificationsDto.builder()
                .receiverId(event.getOwner().getId())
                .targetEntityId(event.getCommentId())
                .relatedEntityId(event.getLikeId())
                .eventType(EventType.COMMENT_LIKED)
                .status(NotificationStatus.PENDING)
                .build();

        PendingNotifications notifications = PendingNotificationsMapper.toEntity(notificationsDto);

        notificationRepository.save(notifications);
    }
}
