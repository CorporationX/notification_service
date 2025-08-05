package faang.school.notificationservice.repository;

import faang.school.notificationservice.model.LastSentNotifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface LastSentNotificationsRepository extends JpaRepository<LastSentNotifications, Long> {

    @Modifying
    @Query(
            nativeQuery = true,
            value = """
                    DELETE FROM last_sent_notifications
                    WHERE last_sent_at < :cleanupThreshold
                    """
    )
    void cleanOldLastSentNotifications(@Param("cleanupThreshold") LocalDateTime cleanupThreshold);
}
