package faang.school.notificationservice.repository;

import faang.school.notificationservice.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, String> {
}
