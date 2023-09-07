package faang.school.notificationservice.command.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CommandExecutor {
    private final Map<String, Command> commands;

    public SendMessage executeCommand(long chatId, String userName, String textCommand) {
        Command command = commands.getOrDefault(textCommand, commands.get("/error"));
        return command.execute(chatId,userName,textCommand);
    }
}


