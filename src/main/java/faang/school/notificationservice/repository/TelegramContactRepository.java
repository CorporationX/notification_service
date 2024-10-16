package faang.school.notificationservice.repository;

import faang.school.notificationservice.model.TelegramContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramContactRepository extends JpaRepository<TelegramContact, Long> {

    @Query(nativeQuery = true, value = """
            insert into telegram_contacts(username, chat_id)
            values(?1, ?2)
            on conflict (username) do update
            set chat_id = ?2
            returning id
            """)
    void addChatIdForUser(String username, Long chatId);

    @Query(nativeQuery = true, value = """
            select tc.chat_id from telegram_contacts tc
            where tc.username = ?1
            """)
    Long findChatIdByUsername(String username);
}
