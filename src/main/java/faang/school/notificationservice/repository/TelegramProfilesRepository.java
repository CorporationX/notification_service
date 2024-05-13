package faang.school.notificationservice.repository;

import faang.school.notificationservice.entity.TelegramProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelegramProfilesRepository extends JpaRepository<TelegramProfile, Long> {

    boolean existsByUserName(String userName);

    Optional<TelegramProfile> findByUserId(Long userId);
}
