package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.bot.TelegramBotCache;
import faang.school.notificationservice.model.TelegramContact;
import faang.school.notificationservice.repository.TelegramContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramContactService {
    private final TelegramBotCache cache;
    private final TelegramContactRepository repository;

    public void addInfoToCache(String username, Long chatId) {
        log.info("User %s have subscribed".formatted(username));
        cache.add(username, chatId);
    }

    public void addChatIdForUser(String username, Long chatId) {
        repository.addChatIdForUser(username, chatId);
    }

    public Long getChatIdForUser(String username) {
        return repository.findChatIdByUsername(username);
    }
}
