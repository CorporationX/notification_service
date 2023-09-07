package faang.school.notificationservice.command.telegram;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ContactDto;
import faang.school.notificationservice.entity.TelegramProfile;
import faang.school.notificationservice.service.telegram.TelegramProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class StartCommand extends Command {
    private final TelegramProfileService telegramProfileService;
    private final UserServiceClient userServiceClient;

    public StartCommand(MessageSource messageSource, TelegramProfileService telegramProfileService, UserServiceClient userServiceClient) {
        super(messageSource);
        this.telegramProfileService = telegramProfileService;
        this.userServiceClient = userServiceClient;
    }

    @Override
    public SendMessage execute(long chatId, String userName) {
        messageSource.getMessage("telegram.start");
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

        return null;
    }
}
