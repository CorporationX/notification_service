package faang.school.notificationservice.repository;

import faang.school.notificationservice.entity.TelegramAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TelegramAccountRepository extends JpaRepository<TelegramAccount, UUID> {
    Optional<TelegramAccount> findByUserId (long userId);

    Optional<TelegramAccount> findByChatId(long chatId);


}
