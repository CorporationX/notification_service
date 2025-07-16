package faang.school.notificationservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pending_notifications")
public class PendingNotifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "recipient_id", nullable = false)
    private long recipientId;

    @Column(name = "notification_type", length = 50, nullable = false)
    private String notificationType;

    @Column(name = "status", length = 50, nullable = false)
    private String status;

    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime createdAt;

    @Column(name = "sent_at", columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime sentAt;

    @Column(name = "error_text", columnDefinition = "TEXT")
    private String errorText;

    @Column(name = "retry_count")
    private int retryCount = 0;
}