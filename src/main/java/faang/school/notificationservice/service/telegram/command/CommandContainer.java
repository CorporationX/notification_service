package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.service.telegram.TelegramBot;
import faang.school.notificationservice.service.telegram.command.impl.UnknownCommand;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CommandContainer {
    private final TelegramCommand unknownCommand;
    @Getter
    private final Map<String, TelegramCommand> commands = new HashMap<>();

    public CommandContainer(TelegramBot telegramBot, List<TelegramCommand> commandList) {
        this.unknownCommand = new UnknownCommand();
        this.unknownCommand.setBot(telegramBot);
        for (TelegramCommand command : commandList) {
            command.setBot(telegramBot);
            commands.put(command.getCommandName().toLowerCase(), command);
        }
    }

    public TelegramCommand getCommand(String commandName) {
        return commands.getOrDefault(commandName.toLowerCase(), unknownCommand);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CommandContainer{");
        sb.append("unknownCommand=").append(unknownCommand.getClass().getName());
        sb.append(", commands={");
        commands.forEach((key, value) -> {
            sb.append(key).append('=');
            sb.append(value.getClass().getName()).append(";");
        });
        sb.append('}');
        sb.append('}');
        return sb.toString();
    }
}
