package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramService implements NotificationService {

    private final TelegramClient telegramClient;
    @SneakyThrows
    @Override
    public void send(UserDto user, String message) {
        SendMessage tgMessage = SendMessage.builder()
                .chatId(user.getTgChatId())
                .text(message)
                .build();

        telegramClient.execute(tgMessage);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
