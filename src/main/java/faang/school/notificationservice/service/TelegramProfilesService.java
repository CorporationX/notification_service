package faang.school.notificationservice.service;

import faang.school.notificationservice.entity.TelegramProfiles;
import faang.school.notificationservice.repository.TelegramProfilesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TelegramProfilesService {

    private final TelegramProfilesRepository telegramProfilesRepository;

    public boolean existsByChatId(Long chatId) {
        return telegramProfilesRepository.existsByChatId(chatId);
    }

    @Transactional
    public TelegramProfiles save(TelegramProfiles telegramProfiles) {
        return telegramProfilesRepository.save(telegramProfiles);
    }

    public TelegramProfiles findByUserId(Long userId) {
        return telegramProfilesRepository.findByUserId(userId);
    }
}
