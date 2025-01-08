package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.repository.TelegramChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramService{
    private final UserServiceClient userServiceClient;
    private final TelegramChatRepository telegramChatRepository;

    @Async("telegramBotExecutor")
    protected CompletableFuture<String> authorizeUser(long chatId, String phoneNumber) {
        Long userId = userServiceClient.findUserByPhone(phoneNumber);

        if (userId != null) {
            saveOrUpdateChatId(chatId, userId);
            return CompletableFuture.completedFuture("Authorization was successful, welcome!");
        } else {
            return CompletableFuture.completedFuture("Authorization failed. Are you sure you've registered?");
        }
    }

    @Async("telegramBotExecutor")
    public void saveOrUpdateChatId(long chatId, long userId){
        telegramChatRepository.saveOrUpdateChatId(chatId, userId);
    }

    @Async("telegramBotExecutor")
    public CompletableFuture<Long> findChatIdByUserId(long userId){
        return CompletableFuture.completedFuture(telegramChatRepository.findChatIdByUserId(userId));
    }
}
