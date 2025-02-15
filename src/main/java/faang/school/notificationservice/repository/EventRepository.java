package faang.school.notificationservice.repository;

import faang.school.notificationservice.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    int deleteByProcessedAtBefore(LocalDateTime dateTime);
}
