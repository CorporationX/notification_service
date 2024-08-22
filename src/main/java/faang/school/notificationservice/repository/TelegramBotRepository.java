package faang.school.notificationservice.repository;

import faang.school.notificationservice.model.TelegramChatBot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelegramBotRepository extends JpaRepository<TelegramChatBot, Long> {

    Optional<Long> findChatIdByUserId(Long userId);

    void deleteByChatId(long chatId);
}