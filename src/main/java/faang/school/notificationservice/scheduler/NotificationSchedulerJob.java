package faang.school.notificationservice.scheduler;

import faang.school.notificationservice.dto.notification.AggregatedNotificationsDto;
import faang.school.notificationservice.model.PendingNotifications;
import faang.school.notificationservice.repository.NotificationRepository;
import faang.school.notificationservice.service.notification.NotificationAggregationService;
import faang.school.notificationservice.service.notification.NotificationSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationSchedulerJob {

    private final NotificationRepository notificationRepository;
    private final NotificationSenderService notificationSender;
    private final NotificationAggregationService aggregator;

    @Value("${notification-scheduler.notification-delay-hours}")
    private int delayHours;

    @Value("${notification-scheduler.last-sent-threshold-hours}")
    private int lastSentThresholdHours;

    @Value("${notification-scheduler.maxRetryAttempts}")
    private int maxRetryAttempts;

    @Scheduled(cron = "${notification-scheduler.cron}")
    @Transactional
    public void publishNotifications() {
        LocalDateTime notificationDelay = LocalDateTime.now().minusHours(delayHours);
        LocalDateTime lastSentThreshold = LocalDateTime.now().minusHours(lastSentThresholdHours);

        List<PendingNotifications> pendingNotifications = notificationRepository
                .findAndLockPendingNotifications(notificationDelay, lastSentThreshold, maxRetryAttempts);

        if (pendingNotifications.isEmpty()) {
            log.info("No pending notifications to send");
            return;
        }

        List<AggregatedNotificationsDto> aggregatedNotifications = aggregator
                .aggregateNotifications(pendingNotifications);

        aggregatedNotifications.forEach(notificationSender::sendAggregatedNotifications);

        log.info("Notifications sent successfully");
    }
}