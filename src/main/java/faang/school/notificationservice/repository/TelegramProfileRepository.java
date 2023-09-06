package faang.school.notificationservice.repository;

import faang.school.notificationservice.entity.TelegramProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramProfileRepository extends JpaRepository<TelegramProfile, Long> {
    boolean existsByUserName(String userName);

}
