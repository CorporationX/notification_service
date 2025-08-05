package faang.school.notificationservice.repository;

import faang.school.notificationservice.model.PendingNotifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<PendingNotifications, Long> {

    @Query(
            nativeQuery = true,
            value = """
                        WITH candidate_ids AS (
                            SELECT
                                MIN(pn.id) AS id
                            FROM pending_notifications pn
                            LEFT JOIN last_sent_notifications lsn
                              ON lsn.receiver_id = pn.receiver_id
                             AND lsn.target_entity_id = pn.target_entity_id
                             AND lsn.event_type = pn.event_type
                            WHERE
                                pn.status = 'PENDING'
                                AND pn.created_at <= :notificationDelay
                                AND (lsn.last_sent_at IS NULL OR lsn.last_sent_at < :lastSentThreshold)
                                AND (
                                    :totalInstances = 1
                                    OR MOD(HASHTEXT(CONCAT(pn.target_entity_id, ':', pn.event_type)), :totalInstances) = :currentInstance
                                )
                            GROUP BY pn.receiver_id, pn.target_entity_id, pn.event_type
                            ORDER BY MIN(pn.created_at)
                            LIMIT 1000
                        )
                        SELECT pn.*
                        FROM pending_notifications pn
                        JOIN candidate_ids ci ON pn.id = ci.id
                        FOR UPDATE SKIP LOCKED
                    """
    )
    List<PendingNotifications> findAndLockPendingNotifications(
            @Param("notificationDelay") LocalDateTime notificationDelay,
            @Param("lastSentThreshold") LocalDateTime lastSentThreshold,
            @Param("totalInstances") int totalInstances,
            @Param("currentInstance") int currentInstance
    );

    @Modifying
    @Transactional
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