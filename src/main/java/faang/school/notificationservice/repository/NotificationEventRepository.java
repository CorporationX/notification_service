package faang.school.notificationservice.repository;

import faang.school.notificationservice.model.NotificationEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationEventRepository extends CrudRepository<NotificationEvent, Long> {
}
