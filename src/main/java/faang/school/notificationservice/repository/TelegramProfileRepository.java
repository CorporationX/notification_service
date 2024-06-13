package faang.school.notificationservice.repository;

import faang.school.notificationservice.entity.TelegramProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelegramProfileRepository extends JpaRepository<TelegramProfile, Long> {

    Optional<TelegramProfile> findByUserId(long userId);

    boolean existsByUserName(String userName);
}
