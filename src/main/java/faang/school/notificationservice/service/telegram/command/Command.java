package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.service.telegram.TelegramBot;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Command {
    protected final CommandExecutor receiver;
    protected final TelegramBot sender;

    abstract void execute();
}
