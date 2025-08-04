package faang.school.notificationservice.service.notification.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostLikedNotificationEventHandler implements NotificationEventHandler<PostLikedNotificationEvent> {

    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void saveNotifications(List<PostLikedNotificationEvent> events) {
        List<PendingNotifications> notifications = events.stream()
                .map(event -> PendingNotificationsMapper.toEntity(
                        PendingNotificationsDto.builder()
                                .receiverId(event.getOwner().getId())
                                .targetEntityId(event.getPostId())
                                .eventData(objectMapper.valueToTree(event))
                                .eventType(EventType.POST_LIKED)
                                .status(NotificationStatus.PENDING)
                                .build()))
                .toList();

        notificationRepository.saveAll(notifications);
    }
}
