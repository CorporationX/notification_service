package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.model.TelegramChat;
import faang.school.notificationservice.repository.TelegramChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TelegramChatService {
    private final TelegramChatRepository telegramChatRepository;
    private final UserService userService;

    @Transactional
    public String createChatId(Long chatId, Long userId) {
        UserDto userDto = userService.getUser(userId);
        TelegramChat newChat = TelegramChat.builder()
                .chatId(chatId)
                .userId(userDto.getId())
                .build();
        telegramChatRepository.save(newChat);
        return "Hi dear " + userDto.getUsername() + " !! Now you get all notification here";
    }

    @Transactional
    public void deleteChatId(Long chatId) {
        telegramChatRepository.deleteByChatId(chatId);
    }

    @Transactional(readOnly = true)
    public boolean checkTelegramChatIsAlive(Long chatId) {
        return telegramChatRepository.existsByChatId(chatId);
    }

    @Transactional(readOnly = true)
    public Long getChatId(Long userId) {
        return telegramChatRepository.findChatIdByUserId(userId);
    }
}
