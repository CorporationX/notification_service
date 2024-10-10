package faang.school.notificationservice.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@RequiredArgsConstructor
public class TelegramBotCache {

    private final ConcurrentMap<String, Long> userChat = new ConcurrentHashMap<>();

    public Long getChatId(String username) {
        return userChat.get(username);
    }

    public void add(String username, Long chatId) {
        userChat.put(username, chatId);
    }
}
