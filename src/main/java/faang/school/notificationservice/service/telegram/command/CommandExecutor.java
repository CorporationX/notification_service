package faang.school.notificationservice.service.telegram.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandExecutor {

    private final Map<String, Command> commands;

    public SendMessage executeCommand(String command, long chatId, String userName) {
        log.info("Executing command {}", command);
        Command executeCommand = commands.getOrDefault(command, commands.get("/unknown"));
        return executeCommand.sendMessage(chatId, userName);
    }
}
