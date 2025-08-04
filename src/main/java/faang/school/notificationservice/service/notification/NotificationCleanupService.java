package faang.school.notificationservice.service.notification;

import faang.school.notificationservice.repository.LastSentNotificationsRepository;
import faang.school.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationCleanupService {

    private final NotificationRepository notificationRepository;
    private final LastSentNotificationsRepository lastSentNotificationsRepository;

    @Value("${notification-cleanup.cleanup-interval}")
    private int cleanupInterval;

    @Transactional
    public void cleanOldNotifications() {
        try {
            LocalDateTime cleanupThreshold = LocalDateTime.now().minusDays(cleanupInterval);
            notificationRepository.deleteOldNotifications(cleanupThreshold);
            lastSentNotificationsRepository.cleanOldLastSentNotifications(cleanupThreshold);
            log.info("Cleanup of notifications older than {} days has been successfully completed.", cleanupInterval);
        } catch (RuntimeException e) {
            log.error("Error during old notifications cleanup", e);
        }
    }
}