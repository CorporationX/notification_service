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

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentLikedNotificationEventHandler implements NotificationEventHandler<CommentLikedNotificationEvent> {

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public void saveNotifications(List<CommentLikedNotificationEvent> events) {
        List<PendingNotifications> notifications = events.stream()
                .map(event -> PendingNotificationsMapper.toEntity(
                        PendingNotificationsDto.builder()
                                .receiverId(event.owner().getId())
                                .targetEntityId(event.getCommentId())
                                .relatedEntityId(event.getLikeId())
                                .eventType(EventType.COMMENT_LIKED)
                                .status(NotificationStatus.PENDING)
                                .build()))
                .toList();

        notificationRepository.saveAll(notifications);
    }
}
