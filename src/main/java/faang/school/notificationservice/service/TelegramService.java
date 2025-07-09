package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.telegram.UserTelegramDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramService implements NotificationService {
    private final TelegramClient telegramClient;
    private final UserServiceClient userServiceClient;

    @Override
    public void send(UserDto user, String message) {
        log.info("Prepare message [{}] to sending to user [{}]", message, user.getId());
        try {
            UserTelegramDto userTelegramDto = userServiceClient.getUserTelegram(user.getId());
            SendMessage builtMessage = SendMessage.builder()
                    .text(message)
                    .chatId(userTelegramDto.telegramChatId())
                    .build();

            telegramClient.execute(builtMessage);
            log.info("Message [{}] sent to user [{}] successfully", message, user.getId());
        } catch (TelegramApiException e) {
            log.error("Fatal error on sending telegram message [{}] to [{}]", message, user.getId(), e);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
