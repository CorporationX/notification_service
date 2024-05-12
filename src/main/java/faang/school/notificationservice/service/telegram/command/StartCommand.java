package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ContactDto;
import faang.school.notificationservice.entity.TelegramProfiles;
import faang.school.notificationservice.service.telegram.TelegramProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Locale;

@Slf4j
@Component("/start")
public class StartCommand extends Command {

    public StartCommand(MessageSource messageSource,
                        UserServiceClient userServiceClient,
                        TelegramProfileService telegramProfileService) {
        super(messageSource, userServiceClient, telegramProfileService);
    }

    @Override
    public SendMessage message(long chatId, String userName) {
        log.info("Executing command for chatId: {} with userName: {}", chatId, userName);
        String message;

        if (telegramProfileService.existsByChatId(chatId)) {
            message = messageSource.getMessage("telegram.start.already_registered", null, Locale.getDefault());
        } else {
            ContactDto contact = userServiceClient.getContact(userName);
            TelegramProfiles telegramProfiles = createTelegramProfiles(chatId, userName, contact);
            telegramProfilesService

        }

        return null;
    }

    public TelegramProfiles createTelegramProfiles(long chatId, String userName, ContactDto contact) {
        log.info("Creating TelegramProfile with chatId {} userName {}", chatId, userName);
        return TelegramProfiles.builder()
                .userId(contact.getUserId())
                .userName(userName)
                .chatId(chatId)
                .build();
    }
}
