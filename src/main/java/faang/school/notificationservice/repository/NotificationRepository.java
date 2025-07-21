package faang.school.notificationservice.repository;

import faang.school.notificationservice.dto.notification.AggregatedNotificationsDto;
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

    @Query("""
                SELECT new faang.school.notificationservice.dto.notification.AggregatedNotificationsDto(
                         pn.receiverId,
                         pn.targetEntityId,
                         MIN(pn.relatedEntityId),
                         pn.eventType,
                         COUNT(pn)
                       )
                FROM PendingNotifications pn
                WHERE pn.status IN ('PENDING', 'FAILED')
                  AND pn.createdAt <= :notificationDelay
                  AND NOT EXISTS (
                      SELECT 1
                      FROM PendingNotifications sub
                      WHERE sub.status = 'SENT'
                        AND sub.targetEntityId = pn.targetEntityId
                        AND sub.sentAt >= :lastSentThreshold
                  )
                GROUP BY pn.receiverId, pn.targetEntityId, pn.eventType
            """)
    List<AggregatedNotificationsDto> findNotRecentGroupedNotificationsToSend(
            @Param("notificationDelay") LocalDateTime notificationDelay,
            @Param("lastSentThreshold") LocalDateTime lastSentThreshold
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
}