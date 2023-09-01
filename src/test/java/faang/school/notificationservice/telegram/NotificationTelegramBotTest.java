package faang.school.notificationservice.telegram;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.bot.BotConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NotificationTelegramBotTest {
    @Mock
    BotConfig config;
    @Mock
    UserServiceClient userServiceClient;
//    @Mock
//    TelegramLongPollingBot telegramLongPollingBot;
    @InjectMocks
    NotificationTelegramBot notificationTelegramBot;

    @Test
    void testTextUpdate_shouldGetMessageAndButton() {
        Update update = Mockito.mock(Update.class);



    }


}