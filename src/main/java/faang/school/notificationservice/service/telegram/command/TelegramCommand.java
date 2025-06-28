package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.service.telegram.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface TelegramCommand {
    void execute(Update update);

    void setBot(TelegramBot telegramBot);

    String getCommandName();

    String getDescription();
}
