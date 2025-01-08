package faang.school.notificationservice.repository;

import faang.school.notificationservice.entity.TelegramChat;
import feign.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramChatRepository extends JpaRepository<TelegramChat, Long> {

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
                INSERT INTO telegram_chat (chat_id, user_id)
                VALUES (:chatId, :userId)
                ON CONFLICT (chat_id) DO UPDATE SET chat_id = EXCLUDED.chat_id;
            """)
    void saveOrUpdateChatId(@Param("chatId") long chatId, @Param("userId") long userId);

    @Query(nativeQuery = true, value = """
            SELECT c.id* FROM telegram_chat c
            WHERE u.id = : userId
            """)
    Long findChatIdByUserId(@Param("userId") long userId);
}

