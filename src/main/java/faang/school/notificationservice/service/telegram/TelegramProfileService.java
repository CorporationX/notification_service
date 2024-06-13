package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.entity.TelegramProfile;

public interface TelegramProfileService {

    void save(TelegramProfile telegramProfile);

    boolean existsByUserName(String userName);

    TelegramProfile findByUserId(Long userId);
}
