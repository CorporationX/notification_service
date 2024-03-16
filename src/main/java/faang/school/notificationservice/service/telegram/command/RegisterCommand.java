package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.entity.TelegramAccount;
import faang.school.notificationservice.service.telegram.TelegramAccountService;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@Slf4j
public class RegisterCommand extends Command {
    @Value("${telegram.command.register}")
    private String registerCommand;
    private final TelegramAccountService telegramAccountService;
    private final UserServiceClient userServiceClient;

    public RegisterCommand(MessageSource messageSource,
                           TelegramAccountService telegramAccountService,
                           UserServiceClient userServiceClient) {
        super(messageSource);
        this.telegramAccountService = telegramAccountService;
        this.userServiceClient = userServiceClient;
    }

    @Override
    public SendMessage process(long chatId, String text) {
        var telegramAccountOpt = telegramAccountService.findByChatId(chatId);

        if (telegramAccountOpt.isPresent() && telegramAccountOpt.get().isConfirmed()) {
            return buildSendMessage(
                    chatId,
                    getMessage("telegram.register-message.registered.already", null)
            );
        }

        if (telegramAccountOpt.isPresent() && !telegramAccountOpt.get().isConfirmed()) {
            return buildSendMessage(
                    chatId,
                    getMessage("telegram.register-message.registered.new", null)
            );
        }

        String answer = registerUser(chatId, getUsername(text));
        return buildSendMessage(chatId, answer);
    }

    private String registerUser(long chatId, String username) {
        try {
            long userId = userServiceClient.getUserByUsername(username).getId();
            telegramAccountService.save(
                    TelegramAccount.builder()
                            .userId(userId)
                            .chatId(chatId)
                            .build()
            );
            return getMessage("telegram.register-message.registered.new", new Object[]{username});
        } catch (FeignException e) {
            return getMessage("telegram.register-message.notFound", new Object[]{username});
        }
    }

    private String getUsername(String input) {
        return input
                .replace(registerCommand, "")
                .trim();
    }

    @Override
    public boolean isApplicable(String commandText) {
        return commandText.contains(registerCommand);
    }
}