package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.repository.TelegramContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramContactService {
    private final TelegramContactRepository repository;


    public void addChatIdForUser(String username, Long chatId) {
        log.info("User %s have subscribed".formatted(username));
        repository.addChatIdForUser(username, chatId);
    }

    public Long getChatIdForUser(String username) {
        return repository.findChatIdByUsername(username);
    }
}
