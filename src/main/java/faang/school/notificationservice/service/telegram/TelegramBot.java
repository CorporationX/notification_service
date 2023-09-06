package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.telegram.TelegramBotConfig;
import faang.school.notificationservice.dto.ContactDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.entity.TelegramProfile;
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

    private final TelegramProfileService telegramProfileService;
    private final UserServiceClient userServiceClient;
    private final TelegramBotConfig config;

    @Autowired
    public TelegramBot(TelegramProfileService telegramProfileService, UserServiceClient userServiceClient, TelegramBotConfig config) {
        super(config.getToken());
        this.telegramProfileService = telegramProfileService;
        this.userServiceClient = userServiceClient;
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getFrom().getUserName();

            if (messageText.equals("/start")) {
                startBot(chatId, userName);
            } else {
                log.info("Unexpected message");
            }
        }
    }

    public void sendNotification(Long userId, String message) {
        TelegramProfile profile = telegramProfileService.getByUserId(userId);

        if(profile != null){
            SendMessage notification = new SendMessage();
            notification.setChatId(profile.getChatId());
            notification.setText(message);

            try {
                execute(notification);
                log.info("Reply sent");
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        }
    }


    private void startBot(long chatId, String userName) {
        if (telegramProfileService.existsByUserName(userName)) {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Пошел нахуй");

            try {
                execute(message);
                log.info("Reply sent");
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
            return;
        }

        ContactDto contactDto = userServiceClient.getContactByContent(userName);

        TelegramProfile telegramProfile = TelegramProfile.builder()
                .chatId(chatId)
                .userName(userName)
                .userId(contactDto.getUserId())
                .build();

        telegramProfileService.save(telegramProfile);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Hello, " + userName + "! Now you are authorized.");

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
