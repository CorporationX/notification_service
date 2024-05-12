package faang.school.notificationservice.repository;

import faang.school.notificationservice.entity.TelegramProfiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelegramProfilesRepository extends JpaRepository<TelegramProfiles, Long> {

    boolean existsByChatId(Long chatId);

    Optional<TelegramProfiles> findByUserId(Long userId);
}
