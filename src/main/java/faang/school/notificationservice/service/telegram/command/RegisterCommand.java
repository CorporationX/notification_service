package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.entity.TelegramAccount;
import faang.school.notificationservice.service.telegram.TelegramAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegisterCommand extends Command {
    private static final String registerCommand = "/register";
    private final TelegramAccountService telegramAccountService;
    private final UserServiceClient userServiceClient;

    @Override
    public SendMessage build(String text, long chatId) {
        String answer;

        if (telegramAccountService.existsByChatId(chatId)) {
            answer = "You are already registered!";
            return buildSendMessage(answer, chatId);
        }

        long userId = userServiceClient.getUserByUsername(getUsername(text)).getId();
        answer = saveTelegramAccount(chatId, userId);

        return buildSendMessage(answer, chatId);
    }

    private String saveTelegramAccount(long chatId, long userId) {
        if (userServiceClient.existsUserById(userId)) {
            telegramAccountService.save(
                    TelegramAccount.builder()
                            .userId(userId)
                            .chatId(chatId)
                            .build()
            );
            return String.format("Your logged in with user id = %s", userId);
        }

        return String.format("User with id = %s not found", userId);
    }

    @Override
    public boolean isApplicable(String commandText) {
        return commandText.contains(registerCommand);
    }

    private String getUsername(String input) {
        String[] parts = input.split("\\s+");

        for (String part : parts) {
            if (!part.equals(registerCommand)) {
                return part;
            }
        }
        throw new RuntimeException("failed to process command = " + input);
    }

    private SendMessage buildSendMessage(String text, long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
    }
}