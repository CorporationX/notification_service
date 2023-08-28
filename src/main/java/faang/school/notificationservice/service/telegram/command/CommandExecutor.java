package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.service.telegram.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandExecutor {
    private static final String COMMAND_NOT_EXIST = "The transmitted command does not exist: %s";

    private final TelegramBot telegramBot;
    private final List<Command> commands;

    public void executeCommand(String messageText, long chatId, String firstName) {
        Optional<Command> existingCommand = getCommand(messageText);
        existingCommand.ifPresentOrElse(command -> {
            command.execute(chatId, firstName);
            log.info("The command {} from chat {} was executed.", messageText, chatId);
        }, () -> {
            SendMessage message = initMassage(messageText, chatId);
            try {
                telegramBot.execute(message);
                log.warn("A non-existent command was transmitted: {} from chat: {}", messageText, chatId);
            } catch (TelegramApiException e) {
                log.error("Failed to send a message about an invalid command to chat: {}", chatId);
                throw new RuntimeException(e);
            }
        });
    }

    private Optional<Command> getCommand(String messageText) {
        return commands.stream()
                .filter(command -> command.getName().equals(messageText))
                .findFirst();
    }

    private SendMessage initMassage(String messageText, long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(String.format(COMMAND_NOT_EXIST, messageText));
        return sendMessage;
    }
}
