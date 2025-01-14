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
            ON CONFLICT (chat_id) DO UPDATE SET user_id = EXCLUDED.user_id;
            """)
    void saveOrUpdateChatId(@Param("chatId") long chatId, @Param("userId") long userId);

    @Query(nativeQuery = true, value = """
        SELECT c.chat_id FROM telegram_chat c
        WHERE c.user_id = :userId
    """)
    Long findChatIdByUserId(@Param("userId") long userId);

}

