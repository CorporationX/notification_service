package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.service.telegram.TelegramBot;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Getter
@RequiredArgsConstructor
@PropertySource("classpath:messages.properties")
public abstract class Command {
    private final String name;
    protected final TelegramBot receiver;
    protected final Environment environment;

    abstract void execute(SendMessage message);
}
