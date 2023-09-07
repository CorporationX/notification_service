package faang.school.notificationservice.command.telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class StartCommand extends Command {

    @Override
    public SendMessage execute(long chatId, String userName, String textCommand) {
        return null;
    }
}
