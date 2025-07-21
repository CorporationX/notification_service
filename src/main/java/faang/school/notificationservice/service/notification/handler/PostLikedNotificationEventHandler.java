package faang.school.notificationservice.service.notification.handler;

import faang.school.notificationservice.dto.notification.PendingNotificationsDto;
import faang.school.notificationservice.event.kafka.PostLikedNotificationEvent;
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
public class PostLikedNotificationEventHandler implements NotificationEventHandler<PostLikedNotificationEvent> {

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public void saveNotification(PostLikedNotificationEvent event) {
        PendingNotificationsDto notificationsDto = PendingNotificationsDto.builder()
                .receiverId(event.getOwner().getId())
                .targetEntityId(event.getPostId())
                .relatedEntityId(event.getLikeId())
                .eventType(EventType.POST_LIKED)
                .status(NotificationStatus.PENDING)
                .build();

        PendingNotifications notifications = PendingNotificationsMapper.toEntity(notificationsDto);

        notificationRepository.save(notifications);
    }
}
