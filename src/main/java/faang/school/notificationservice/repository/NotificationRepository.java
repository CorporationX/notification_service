package faang.school.notificationservice.repository;

import faang.school.notificationservice.model.PendingNotifications;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Repository
public interface NotificationRepository extends CrudRepository<PendingNotifications, Long> {

    @Query(nativeQuery = true,
            value = """
                    SELECT *
                    FROM pending_notifications
                    WHERE status = 'PENDING'
                    """)
    List<PendingNotifications> findAllPendingNotifications();

    Optional<PendingNotifications> findTopByRecipientIdAndStatusOrderBySentAtDesc(Long recipientId, String status);

}
