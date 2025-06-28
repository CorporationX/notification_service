package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.service.telegram.TelegramBot;
import org.springframework.context.MessageSource;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Locale;
import java.util.Optional;

public abstract class AbstractTelegramCommand implements TelegramCommand {

    protected final MessageSource messageSource;
    private final Locale locale;
    private final String command;
    private final String description;

    protected TelegramBot telegramBot;

    public AbstractTelegramCommand(MessageSource messageSource, Locale locale, String command, String description) {
        this.messageSource = messageSource;
        this.locale = locale;
        this.command = command;
        this.description = description;
    }

    protected Optional<Long> getChatId(Update update) {
        Optional<Message> message = Optional.ofNullable(update.getMessage());
        return message.map(Message::getChatId);
    }

    @Override
    public void setBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Override
    public String getCommandName() {
        return command;
    }

    @Override
    public String getDescription() {
        return messageSource.getMessage(description, null, locale);
    }
}

