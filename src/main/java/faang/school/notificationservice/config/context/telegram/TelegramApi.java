package faang.school.notificationservice.config.context.telegram;

import org.telegram.telegrambots.meta.TelegramBotsApi;

public class TelegramApi {

    ApiContextInitializer.init();
    TelegramBotsApi botsApi = new TelegramBotsApi();
    botsApi.registerBot(new TelegramBot());

}
