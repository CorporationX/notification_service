package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.service.telegram.TelegramBot;
import org.springframework.stereotype.Component;

@Component
public class StartCommand extends Command {
    public StartCommand(CommandExecutor receiver, TelegramBot sender) {
        super(receiver, sender);
    }

    @Override
    void execute() {

    }
}
