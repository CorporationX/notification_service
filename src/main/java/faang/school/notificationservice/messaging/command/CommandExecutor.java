package faang.school.notificationservice.messaging.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CommandExecutor {
    private final Map<String, Command> commands;
    public SendMessage executeCommand(long chatId, String firstName, String textCommand) {
        CommandDto commandDto = new CommandDto(chatId, firstName, textCommand);
        if (commands.containsKey(textCommand)) {
            return commands.get(textCommand).execute(commandDto);
        } else {
            log.warn("Passed a non-existent command from chat = {}", chatId);
            return commands.get("/error").execute(commandDto);
        }
    }
}

}
