package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.model.TelegramChatBot;
import faang.school.notificationservice.repository.TelegramBotRepository;
import faang.school.notificationservice.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramChatService {
    private final TelegramBotRepository telegramBotRepository;
    private final UserService userService;

    @Transactional
    public long findChatIdByUserId(long userId) {
        return telegramBotRepository.findChatIdByUserId(userId)
                .orElseThrow();
    }

    @Transactional
    public void createChatBot(long userId, long chatId) {
        UserDto userDto = userService.getUser(userId);
        if (userDto == null) {
            log.error("Пользователя с таким ID: {} нет", userId);
            throw new NoSuchElementException("Пользователя с таким ID нет");
        }
        TelegramChatBot newChat = TelegramChatBot.builder()
                .chatId(chatId)
                .userId(userDto.getId())
                .build();
        telegramBotRepository.save(newChat);
    }

    @Transactional
    public void deleteChatBot(long chatId) {
        telegramBotRepository.deleteByChatId(chatId);
    }
}