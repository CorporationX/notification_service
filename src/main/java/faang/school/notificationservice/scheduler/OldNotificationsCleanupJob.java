package faang.school.notificationservice.scheduler;

import faang.school.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class OldNotificationsCleanupJob {

    private final NotificationRepository notificationRepository;

    @Value("${notification-cleanup.cleanup-interval}")
    private int cleanupInterval;

    @Scheduled(cron = "${notification-cleanup.cron}")
    @Transactional
    public void cleanOldNotifications() {
        log.info("Started cleaning old notifications...");

        try {
            LocalDateTime cleanupThreshold = LocalDateTime.now().minusDays(cleanupInterval);
            notificationRepository.deleteOldNotifications(cleanupThreshold);
            log.info("Cleanup of notifications older than {} days has been successfully completed.", cleanupInterval);
        } catch (RuntimeException e) {
            log.error("Error during old notifications cleanup", e);
        }
    }
}