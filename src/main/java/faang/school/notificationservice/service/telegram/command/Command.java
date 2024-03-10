package faang.school.notificationservice.service.telegram.command;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@RequiredArgsConstructor
public abstract class Command {
    abstract public SendMessage build (String text, long chatId);

    abstract public boolean isApplicable(String textCommand);
}
