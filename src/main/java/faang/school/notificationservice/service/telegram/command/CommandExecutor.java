package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.dto.CommandDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandExecutor {
    private final Map<String, Command> commands;

    public void executeCommand(long chatId, String firstName, String textCommand) {
        CommandDto commandDto = new CommandDto(chatId, firstName, textCommand);
        if (commands.containsKey(textCommand)) {
            commands.get(textCommand).execute(commandDto);
            log.info("The command {} from chat {} was executed.", textCommand, chatId);
        } else {
            commands.get("/error").execute(commandDto);
            log.warn("Passed a non-existent command from chat = {}", chatId);
        }
    }
}
