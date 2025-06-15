package faang.school.notificationservice.service.telegram.command.impl;

import faang.school.notificationservice.service.telegram.command.AbstractTelegramCommand;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class UnknownCommand extends AbstractTelegramCommand {
    public static final String UNKNOWN_TEXT = "Неизвестная команда. Список команд: %s".formatted(HelpCommand.COMMAND);

    public UnknownCommand() {
        super(null, null, null, null);
    }

    @Override
    public void execute(Update update) {
        Optional<Long> chatId = getChatId(update);
        chatId.ifPresent(id -> getBot().sendMessage(id, UNKNOWN_TEXT));
    }
}
