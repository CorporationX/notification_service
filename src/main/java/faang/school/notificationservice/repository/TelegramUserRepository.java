package faang.school.notificationservice.repository;

import faang.school.notificationservice.entity.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM telegram_user WHERE user_id = :id")
    Optional<TelegramUser> findByUserId(long id);

    @Query(nativeQuery = true, value = "SELECT EXISTS(SELECT 1 FROM telegram_user WHERE chat_id = :chatId)")
    boolean existsByChatId(long chatId);
}
