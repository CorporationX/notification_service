package faang.school.notificationservice.telegramBot.command;

import faang.school.notificationservice.dto.CommandDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static faang.school.notificationservice.telegramBot.command.CommandName.*;

import java.util.Map;

@Slf4j
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
            return commands.get(ERROR.getCommandName()).execute(commandDto);
        }
    }

}
