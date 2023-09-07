package faang.school.notificationservice.command.telegram;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ContactDto;
import faang.school.notificationservice.entity.TelegramProfile;
import faang.school.notificationservice.service.telegram.TelegramProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component(value = "/start")
public class StartCommand extends Command {
    private final TelegramProfileService telegramProfileService;
    private final UserServiceClient userServiceClient;

    @Autowired
    public StartCommand(MessageSource messageSource, TelegramProfileService telegramProfileService, UserServiceClient userServiceClient) {
        super(messageSource);
        this.telegramProfileService = telegramProfileService;
        this.userServiceClient = userServiceClient;
    }

    @Override
    public SendMessage execute(long chatId, String userName) {
        String response;

        if (telegramProfileService.existsByUserName(userName)) {
            ContactDto contactDto = userServiceClient.getContactByContent(userName);
            TelegramProfile telegramProfile = buildTelegramProfile(chatId, userName, contactDto);
            telegramProfileService.save(telegramProfile);

            response = messageSource.getMessage("telegram.start", new Object[]{userName}, LOCALE_DEFAULT);
        } else {
            response = messageSource.getMessage("telegram.on_the_system", null, LOCALE_DEFAULT);
        }

        return buildMessage(chatId, response);
    }

    private SendMessage buildMessage(long chatId, String response) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(response);

        return message;
    }

    private TelegramProfile buildTelegramProfile(long chatId, String userName, ContactDto contactDto) {
        return TelegramProfile.builder()
                .chatId(chatId)
                .userName(userName)
                .userId(contactDto.getUserId())
                .build();
    }
}
