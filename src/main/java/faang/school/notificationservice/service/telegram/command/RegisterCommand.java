package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ContactPreferenceDto;
import faang.school.notificationservice.entity.TelegramProfile;
import faang.school.notificationservice.service.telegram.TelegramProfileService;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component("/register")
public class RegisterCommand extends Command {

    public RegisterCommand(MessageSource messageSource,
                           UserServiceClient userServiceClient,
                           TelegramProfileService telegramProfileService,
                           CommandBuilder commandBuilder) {
        super(messageSource, userServiceClient, telegramProfileService, commandBuilder);
    }

    @Override
    public SendMessage sendMessage(long chatId, String userName) {
        log.info("Executing REGISTER command for chatId: {} with userName: {}", chatId, userName);
        String message;

        if (telegramProfileService.existsByChatId(chatId)) {
            message = messageSource.getMessage("telegram.start.already_registered", null, defaultLocale);
            return commandBuilder.buildMessage(chatId, message);
        }

        try {
            ContactPreferenceDto contact = userServiceClient.getContactPreference(userName);
            TelegramProfile telegramProfile = createTelegramProfile(chatId, userName, contact);
            telegramProfileService.save(telegramProfile);

            log.info("Telegram profile is saved for the user: {}", userName);

            message = messageSource.getMessage("telegram.start.registered", new String[]{userName}, defaultLocale);
        } catch (FeignException exception) {
            log.error("Error occurred while processing the user: ", exception);
            message = messageSource.getMessage("telegram.service_exception", null, defaultLocale);
        }

        return commandBuilder.buildMessage(chatId, message);
    }

    private TelegramProfile createTelegramProfile(long chatId, String userName, ContactPreferenceDto contact) {
        log.info("Creating TelegramProfile with chatId {} userName {}", chatId, userName);
        return TelegramProfile.builder()
                .userId(contact.getUserId())
                .userName(userName)
                .chatId(chatId)
                .build();
    }
}
