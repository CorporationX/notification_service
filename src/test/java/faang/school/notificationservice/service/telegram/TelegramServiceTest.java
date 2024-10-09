package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.config.context.BotConfig;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TelegramServiceTest {
    @InjectMocks
    TelegramService telegramService;

    @Mock
    TelegramBot bot;
    @Mock
    BotConfig config;


    @Test
    void send() throws TelegramApiException {
        UserDto user = new UserDto().builder()
                .telegramChatId(1L)
                .username("username")
                .build();
        String message = "Test message";
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(user.getTelegramChatId());
        sendMessage.setText(message);

        telegramService.send(user, message);

        verify(bot, times(1)).execute(sendMessage);
    }
}