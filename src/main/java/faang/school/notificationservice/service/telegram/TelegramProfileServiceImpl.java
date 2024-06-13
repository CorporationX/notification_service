package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.entity.TelegramProfile;
import faang.school.notificationservice.exception.NotFoundException;
import faang.school.notificationservice.repository.TelegramProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramProfileServiceImpl implements TelegramProfileService {

    private final TelegramProfileRepository telegramProfileRepository;

    @Override
    @Transactional
    public void save(TelegramProfile telegramProfile) {
        telegramProfileRepository.save(telegramProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserName(String userName) {
        return telegramProfileRepository.existsByUserName(userName);
    }

    @Override
    @Transactional(readOnly = true)
    public TelegramProfile findByUserId(Long userId) {
        return telegramProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Telegram profile not found"));
    }
}
