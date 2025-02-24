package faang.school.notificationservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@ToString
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "events", schema = "notifications")
public class Event {
    @Id
    private UUID id;

    @Column(name = "processed_at", nullable = false, updatable = false, insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime processedAt;

    public Event(UUID id) {
        this.id = id;
    }
}
