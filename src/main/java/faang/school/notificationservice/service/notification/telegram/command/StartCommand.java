package faang.school.notificationservice.service.notification.telegram.command;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ContactDto;
import faang.school.notificationservice.entity.TelegramProfiles;
import faang.school.notificationservice.service.TelegramProfilesService;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component("/start")
public class StartCommand extends Command {

    @Autowired
    public StartCommand(MessageSource messageSource, TelegramProfilesService telegramProfilesService,
                        UserServiceClient userServiceClient) {
        super(messageSource, telegramProfilesService, userServiceClient);
    }

    @Override
    @Retryable(retryFor = FeignException.class)
    public SendMessage execute(long chatId, String nickname) {
        String message;
        if (telegramProfilesService.existsByChatId(chatId)) {
            message = messageSource.getMessage("telegram.start.not_registered", null, defaultLocale);
        } else {
                ContactDto contact = userServiceClient.getContact(nickname);
//                ContactDto contact = new ContactDto(1L, 1L, "setooooon", ContactType.TELEGRAM);
                TelegramProfiles telegramProfiles = createTelegramProfiles(chatId, nickname, contact);
                telegramProfilesService.save(telegramProfiles);
                message = messageSource.getMessage("telegram.start.registered", new String[]{nickname}, defaultLocale);

        }
        return buildSendMessage(chatId, message);
    }

    @Recover
    private SendMessage recover(FeignException e, long chatId, String nickname) {
        String message = messageSource.getMessage("telegram.start.not_registered_corporationX", null, defaultLocale);
        return buildSendMessage(chatId, message);
    }

    private SendMessage buildSendMessage(long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        return sendMessage;
    }

    private TelegramProfiles createTelegramProfiles(long chatId, String nickname, ContactDto contact) {
        return TelegramProfiles.builder()
                .nickname(nickname)
                .userId(contact.getUserId())
                .chatId(chatId)
                .build();
    }
}
