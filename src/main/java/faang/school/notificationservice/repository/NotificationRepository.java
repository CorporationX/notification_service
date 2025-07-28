package faang.school.notificationservice.repository;

import faang.school.notificationservice.model.PendingNotifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<PendingNotifications, Long> {

    @Query(nativeQuery = true,
            value = """
                    SELECT *
                    FROM pending_notifications pn
                    WHERE pn.status IN ('PENDING', 'FAILED')
                    AND retry_count < :maxRetryAttempts
                      AND pn.created_at <= :notificationDelay
                      AND NOT EXISTS (
                          SELECT 1
                          FROM pending_notifications sub
                          WHERE sub.status = 'SENT'
                            AND sub.target_entity_id = pn.target_entity_id
                            AND sub.sent_at >= :lastSentThreshold
                      )
                    FOR UPDATE SKIP LOCKED
                    LIMIT 10000
                    """)
    List<PendingNotifications> findAndLockPendingNotifications(
            @Param("notificationDelay") LocalDateTime notificationDelay,
            @Param("lastSentThreshold") LocalDateTime lastSentThreshold,
            @Param("maxRetryAttempts") int maxRetryAttempts
    );

    @Modifying
    @Query(nativeQuery = true,
            value = """
                    UPDATE pending_notifications
                    SET status = :status, sent_at = NOW()
                    WHERE receiver_id = :receiverId
                      AND target_entity_id = :targetEntityId
                      AND event_type = :eventType
                    """)
    void updateStatusByGroup(
            @Param("receiverId") Long receiverId,
            @Param("targetEntityId") Long targetEntityId,
            @Param("eventType") String eventType,
            @Param("status") String status
    );

    @Modifying
    @Query(nativeQuery = true,
            value = """
                    DELETE FROM pending_notifications pn
                    WHERE  created_at <= :cleanupThreshold
                    """
    )
    void deleteOldNotifications(@Param("cleanupThreshold") LocalDateTime cleanupThreshold);
}