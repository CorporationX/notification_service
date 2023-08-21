package faang.school.notificationservice.config.telegram;

import faang.school.notificationservice.service.telegram.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


@Component
@RequiredArgsConstructor
public class TelegramConfig {

    private final TelegramService telegramService;

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException{
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(telegramService);
    }

//    @Bean
//    public TelegramBotsApi registerBot(){
//        try {
//            TelegramBotsApi botsApi = new TelegramBotsApi(TelegramService.class);
//            botsApi.registerBot(telegramService);
//            return botsApi;
//        } catch (TelegramApiException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
