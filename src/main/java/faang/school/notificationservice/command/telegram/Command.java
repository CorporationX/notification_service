package faang.school.notificationservice.command.telegram;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public abstract class Command {

    public abstract SendMessage execute(long chatId, String userName, String textCommand);
}
