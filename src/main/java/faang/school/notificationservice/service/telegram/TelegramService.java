package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramService implements NotificationService {

    private final TelegramBot telegramBot;

    @Override
    public void send(UserDto user, String message) {
        SendMessage tgMessage = SendMessage.builder()
                .chatId(user.getId())
                .text(message)
                .build();

        try {
            telegramBot.execute(tgMessage);
            log.info("Message was sent to user {} device!", tgMessage.getChatId());
        } catch (TelegramApiException e) {
            log.error("Error during sending telegram notification to user: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
