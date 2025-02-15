package faang.school.notificationservice.service;

import faang.school.notificationservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class DatabaseCleanupJob {
    private final EventRepository eventRepository;

    @Value("${cleanup.job.hours-interval}")
    private int cleanupHours;

    @Transactional
    @Scheduled(cron = "${cleanup.job.cron}")
    public void cleanupDatabase() {
        log.info("Starting database cleanup job");

        LocalDateTime cutoffDate = LocalDateTime.now().minusHours(cleanupHours);
        int deletedEvents = eventRepository.deleteByProcessedAtBefore(cutoffDate);

        log.info("Database cleanup job finished. Deleted {} events.", deletedEvents);
    }
}
