package faang.school.notificationservice.entity;

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
import org.hibernate.annotations.ColumnDefault;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "telegram_profile")
public class TelegramProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "chat_id", nullable = false)
    private long chatId;

    @Column(name = "is_active", nullable = false)
    @ColumnDefault(value = "false")
    private boolean isActive;
}
