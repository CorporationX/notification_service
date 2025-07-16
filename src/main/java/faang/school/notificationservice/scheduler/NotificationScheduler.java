package faang.school.notificationservice.scheduler;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.model.PendingNotifications;
import faang.school.notificationservice.repository.NotificationRepository;
import faang.school.notificationservice.service.notification.NotificationSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationScheduler {

    private final NotificationRepository notificationRepository;
    private final NotificationSenderService notificationSender;
    private final UserServiceClient userServiceClient;

    private static final long BATCHING_DELAY_HOURS = 24;

    @Scheduled(cron = "${notification-scheduler.cron}")
    public void processPendingNotifications() {
        List <PendingNotifications> notifications = notificationRepository.findAllPendingNotifications();
    }

    private UserDto getUserDto(Long userId) {
        return userServiceClient.getUser(userId);
    }
}