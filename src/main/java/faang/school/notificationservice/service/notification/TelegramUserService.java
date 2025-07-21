package faang.school.notificationservice.service.notification;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.entity.telegram.TelegramUser;
import faang.school.notificationservice.repository.telegram.TelegramUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TelegramUserService {
    private final TelegramUserRepository telegramUserRepository;
    private final UserServiceClient userServiceClient;

    @Transactional
    public void saveUserChatId(long userId, long chatId) {
        UserDto user = userServiceClient.getUser(userId);
        TelegramUser telegramUser = TelegramUser.builder()
                .userId(user.getId())
                .telegramChatId(chatId)
                .build();
        telegramUserRepository.save(telegramUser);
    }

    @Transactional(readOnly = true)
    public Optional<Long> getTelegramChatIdByUserId(long userId) {
        return telegramUserRepository.findByUserId(userId)
                .map(TelegramUser::getTelegramChatId);
    }
}
