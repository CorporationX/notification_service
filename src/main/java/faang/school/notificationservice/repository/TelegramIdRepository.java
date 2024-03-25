package faang.school.notificationservice.repository;

import faang.school.notificationservice.entity.TelegramId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelegramIdRepository extends CrudRepository<TelegramId, Long> {
    Optional<TelegramId> findByUserId(Long userId);
}
