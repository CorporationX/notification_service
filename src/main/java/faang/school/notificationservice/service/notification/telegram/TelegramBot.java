package faang.school.notificationservice.service.notification.telegram;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.TelegramBotConfiguration;
import faang.school.notificationservice.dto.ContactDto;
import faang.school.notificationservice.dto.ContactType;
import faang.school.notificationservice.entity.TelegramProfiles;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final TelegramProfilesService telegramProfilesService;
    private final TelegramBotConfiguration telegramBotConfiguration;
    private final UserServiceClient userServiceClient;

    @Autowired
    public TelegramBot(TelegramBotConfiguration telegramBotConfiguration, UserServiceClient userServiceClient,
                       TelegramProfilesService telegramProfilesService) {
        super(telegramBotConfiguration.getToken());
        this.telegramBotConfiguration = telegramBotConfiguration;
        this.userServiceClient = userServiceClient;
        this.telegramProfilesService = telegramProfilesService;
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getFrom().getUserName();

            switch (messageText) {
                case "/start" -> startBot(chatId, userName);

                default -> log.info("Unexpected message");
            }
        }
    }

    private void startBot(long chatId, String userName) {
        boolean existsByChatId = telegramProfilesService.existsByChatId(chatId);
        SendMessage message = new SendMessage();
        if (existsByChatId) {
            message.setChatId(chatId);
            message.setText("Вы уже зарегистрированы!!!");
        } else {
//            ContactDto contact = userServiceClient.getContact(userName);
            ContactDto contact = new ContactDto(1L, 1L, "setooooon", ContactType.TELEGRAM);

            TelegramProfiles telegramProfiles = TelegramProfiles.builder()
                    .nickname(userName)
                    .userId(contact.getUserId())
                    .chatId(chatId)
                    .build();

            telegramProfilesService.save(telegramProfiles);

            message.setChatId(chatId);
            message.setText("Привет, " + userName + "! Я CorporationX werewolf Bot. Напишите /help для получения списка команд");
        }

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return telegramBotConfiguration.getName();
    }
}
