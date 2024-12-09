package faang.school.notificationservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "sms_messages")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmsMessage {

    @Id
    private Long uid;

    @Column(name = "content", length = 4096)
    private String content;

    @Column(name = "receiverId")
    private Long receiverId;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status")
    private MessageDeliveryStatus deliveryStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "send_time")
    private LocalDateTime sendTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "received_time")
    private LocalDateTime receivedTime;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "cost")
    private double cost;
}
