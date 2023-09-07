package faang.school.notificationservice.service.notification.telegram.command;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ContactDto;
import faang.school.notificationservice.dto.ContactType;
import faang.school.notificationservice.entity.TelegramProfiles;
import faang.school.notificationservice.service.TelegramProfilesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component("/start")
public class StartCommand extends Command {

    public StartCommand(MessageSource messageSource, TelegramProfilesService telegramProfilesService,
                        UserServiceClient userServiceClient) {
        super(messageSource, telegramProfilesService, userServiceClient);
    }

    @Override
    public SendMessage execute(long chatId, String userName) {
        boolean existsByChatId = telegramProfilesService.existsByChatId(chatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (existsByChatId) {
            String message = messageSource.getMessage("telegram.start.not_registered", null, defaultLocale);
            sendMessage.setText(message);
        } else {
//            ContactDto contact = userServiceClient.getContact(userName);
            ContactDto contact = new ContactDto(1L, 1L, "setooooon", ContactType.TELEGRAM);

            TelegramProfiles telegramProfiles = TelegramProfiles.builder()
                    .nickname(userName)
                    .userId(contact.getUserId())
                    .chatId(chatId)
                    .build();

            telegramProfilesService.save(telegramProfiles);

            String message = messageSource.getMessage("telegram.start.registered", new String[]{userName}, defaultLocale);
            sendMessage.setText(message);
        }
        return sendMessage;
    }
}
