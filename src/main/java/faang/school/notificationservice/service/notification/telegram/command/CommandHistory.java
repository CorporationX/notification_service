package faang.school.notificationservice.service.notification.telegram.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandHistory {

    private final Map<String, Command> commands;

    public SendMessage execute(String command, long chatId, String nickname) {
        log.info("Executing command {}", command);
        Command commandToExecute = commands.getOrDefault(command, commands.get("/unknown"));
        return commandToExecute.execute(chatId, nickname);
    }
}
