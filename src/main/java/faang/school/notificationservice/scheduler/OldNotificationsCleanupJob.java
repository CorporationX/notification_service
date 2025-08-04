package faang.school.notificationservice.scheduler;

import faang.school.notificationservice.service.notification.NotificationCleanupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OldNotificationsCleanupJob {

    private final NotificationCleanupService notificationCleanupService;

    @Scheduled(cron = "${notification-cleanup.cron}")
    public void triggerOldNotificationsCleanup() {
        log.info("Started cleaning old notifications...");
        notificationCleanupService.cleanOldNotifications();
    }
}