package faang.school.notificationservice.entity.telegram;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "telegram_users")
public class TelegramUser {

    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private long userId;

    @Column(name = "telegram_chat_id", nullable = false, unique = true)
    private long telegramChatId;

}
