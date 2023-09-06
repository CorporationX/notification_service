package faang.school.notificationservice.repository;

import faang.school.notificationservice.entity.TelegramProfiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramProfilesRepository extends JpaRepository<TelegramProfiles, Long> {

    boolean existsByChatId(Long chatId);

    TelegramProfiles findByUserId(Long userId);
}
