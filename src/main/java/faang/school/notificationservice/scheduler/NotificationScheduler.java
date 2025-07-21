package faang.school.notificationservice.scheduler;

import faang.school.notificationservice.dto.notification.AggregatedNotificationsDto;
import faang.school.notificationservice.repository.NotificationRepository;
import faang.school.notificationservice.service.notification.NotificationSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationScheduler {

    private final NotificationRepository notificationRepository;
    private final NotificationSenderService notificationSender;

    private static final LocalDateTime NOTIFICATION_DELAY = LocalDateTime.now().minusHours(1);
    private static final LocalDateTime LAST_SENT_THRESHOLD = LocalDateTime.now().minusHours(24);

    @Scheduled(cron = "${notification-scheduler.cron}")
    @Transactional
    public void publishNotifications() {


        List<AggregatedNotificationsDto> notifications = notificationRepository
                .findNotRecentGroupedNotificationsToSend(NOTIFICATION_DELAY, LAST_SENT_THRESHOLD);

        if (notifications.isEmpty()) {
            log.info("No pending notifications to send");
            return;
        }

        try {
            notifications.forEach(notificationSender::sendAggregatedNotifications);
        } catch (RuntimeException e) {
            log.error("Failed to send notification", e);
        }
    }
}