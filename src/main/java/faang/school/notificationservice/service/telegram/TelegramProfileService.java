package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.entity.TelegramProfile;
import faang.school.notificationservice.exception.EntityNotFoundException;
import faang.school.notificationservice.repository.TelegramProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TelegramProfileService {
    private final TelegramProfileRepository telegramProfileRepository;

    @Transactional
    public void save(TelegramProfile telegramProfile) {
        telegramProfileRepository.save(telegramProfile);
    }

    @Transactional(readOnly = true)
    public boolean existsByUserName(String userName) {
        return telegramProfileRepository.existsByUserName(userName);
    }

    public TelegramProfile getByUserId(long userId) {
        return telegramProfileRepository.getByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Telegram profile not found"));
    }
}
