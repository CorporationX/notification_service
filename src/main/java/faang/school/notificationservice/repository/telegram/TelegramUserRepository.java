package faang.school.notificationservice.repository.telegram;

import faang.school.notificationservice.entity.telegram.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {

    Optional<TelegramUser> findByUserId(Long userId);

}
