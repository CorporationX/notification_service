package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.entity.TelegramProfiles;
import faang.school.notificationservice.repository.TelegramProfilesRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TelegramProfileService {

    private final TelegramProfilesRepository telegramProfilesRepository;

    @Transactional
    public void save(TelegramProfiles telegramProfiles) {
        telegramProfilesRepository.save(telegramProfiles);
    }

    public boolean existsByChatId(Long chatId) {
        return telegramProfilesRepository.existsByChatId(chatId);
    }

    public TelegramProfiles findByUserId(Long userId) {
        return telegramProfilesRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Telegram profile not found"));
    }
}