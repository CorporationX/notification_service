package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.service.telegram.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandExecutor {
    private final TelegramBot receiver;
    private final List<Command> commands;

    public void executeCommand(String messageText, long chatId, String firstName) {
        SendMessage message = new SendMessage();
        Optional<Command> existingCommand = getCommand(messageText);
        existingCommand.ifPresentOrElse(command -> {
            command.execute();
            log.info("");
        }, () -> {

        });
    }

    private Optional<Command> getCommand(String messageText) {
        return commands.stream()
                .filter(command -> command.getName().equals(messageText))
                .findFirst();
    }

    private SendMessage initMessage(long chatId, String firstName) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.s
    }
}
