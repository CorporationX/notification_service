package faang.school.notificationservice.scheduler;

import faang.school.notificationservice.dto.notification.AggregatedNotificationsDto;
import faang.school.notificationservice.repository.NotificationRepository;
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
public class NotificationScheduler {

    private final NotificationRepository notificationRepository;
    private final NotificationSenderService notificationSender;

    @Value("${notification-scheduler.notification-delay-hours}")
    private static int delayHours;

    @Value("${notification-scheduler.last-sent-threshold-hours}")
    private static int lastSentThresholdHours;

    private static final LocalDateTime NOTIFICATION_DELAY = LocalDateTime.now().minusHours(delayHours);
    private static final LocalDateTime LAST_SENT_THRESHOLD = LocalDateTime.now().minusHours(lastSentThresholdHours);

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