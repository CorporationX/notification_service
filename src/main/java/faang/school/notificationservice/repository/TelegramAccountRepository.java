package faang.school.notificationservice.repository;

import faang.school.notificationservice.entity.TelegramAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelegramAccountRepository extends JpaRepository<TelegramAccount, Long> {
    Optional<TelegramAccount> findByUserId (long userId);

    boolean existsByChatId(long chatId);
}
