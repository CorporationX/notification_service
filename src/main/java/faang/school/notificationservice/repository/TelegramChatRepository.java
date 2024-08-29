package faang.school.notificationservice.repository;

import faang.school.notificationservice.model.TelegramChat;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramChatRepository extends JpaRepository<TelegramChat, Long> {

    void deleteByChatId(Long chatId);

    boolean existsByChatId(Long chatId);

    @Query("SELECT t.chatId FROM TelegramChat t WHERE t.userId = :userId")
    Long findChatIdByUserId(@Param("userId") Long userId);
}
