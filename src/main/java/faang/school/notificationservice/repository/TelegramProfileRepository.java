package faang.school.notificationservice.repository;

import faang.school.notificationservice.entity.TelegramProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelegramProfileRepository extends JpaRepository<TelegramProfile, Long> {
    boolean existsByUserName(String userName);

}
