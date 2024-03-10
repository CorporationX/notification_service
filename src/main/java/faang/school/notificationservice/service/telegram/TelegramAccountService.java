package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.entity.TelegramAccount;
import faang.school.notificationservice.repository.TelegramAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramAccountService {
    private final TelegramAccountRepository telegramAccountRepository;

    @Transactional
    public void save(TelegramAccount telegramAccount) {
        telegramAccountRepository.save(telegramAccount);
    }

    @Transactional(readOnly = true)
    public long getChatIdByUserId(long userId) {
        return telegramAccountRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Telegram account not found by user id = " + userId))
                .getChatId();
    }

    @Transactional(readOnly = true)
    public boolean existsByChatId(long chatId) {
        return telegramAccountRepository.existsByChatId(chatId);
    }
}
