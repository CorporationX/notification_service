package faang.school.notificationservice.service.notification.telegram;

import faang.school.notificationservice.entity.TelegramAccount;
import faang.school.notificationservice.repository.TelegramAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramAccountService {
    private final TelegramAccountRepository telegramAccountRepository;

    @Transactional
    public void save(TelegramAccount telegramAccount) {
        telegramAccount.setConfirmed(false);
        telegramAccountRepository.save(telegramAccount);
    }

    @Transactional
    public void confirmAccount(UUID uuid) {
        TelegramAccount telegramAccount = telegramAccountRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Wrong confirmation link"));
        telegramAccount.setConfirmed(true);
    }

    @Transactional(readOnly = true)
    public TelegramAccount getByUserId(long userId) {
        return telegramAccountRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Telegram account not found by user id = " + userId));
    }

    @Transactional(readOnly = true)
    public TelegramAccount getByChatId(long chatId) {
        return telegramAccountRepository.findByChatId(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Telegram account not found by chat id = " + chatId));
    }

    @Transactional(readOnly = true)
    public Optional<TelegramAccount> findByChatId(long chatId) {
        return telegramAccountRepository.findByChatId(chatId);
    }
}
