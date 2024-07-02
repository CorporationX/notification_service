package faang.school.notificationservice.repository;

import faang.school.notificationservice.model.TelegramChat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelegramRepository extends CrudRepository<TelegramChat, Long> {
    Optional<TelegramChat> findByPostAuthorId(Long postAuthorId);
}
