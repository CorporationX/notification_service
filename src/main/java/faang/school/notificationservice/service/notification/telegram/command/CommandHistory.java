package faang.school.notificationservice.service.notification.telegram.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CommandHistory {

    private final Map<String, Command> commands;

    public SendMessage execute(String command, long chatId, String nickname) {
        Command commandToExecute = commands.getOrDefault(command, commands.get("/unknown"));
        return commandToExecute.execute(chatId, nickname);
    }
}
