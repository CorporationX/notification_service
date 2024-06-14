package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.entity.TelegramProfile;

import java.util.Optional;

public interface TelegramProfileService {

    void save(TelegramProfile telegramProfile);

    TelegramProfile findByUserId(Long userId);

    Optional<TelegramProfile> findByChatId(long chatId);

    boolean existsByChatId(long chatId);
}
