package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.dto.CommandDto;
import faang.school.notificationservice.service.telegram.TelegramBot;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Getter
@RequiredArgsConstructor
@PropertySource("classpath:messages.properties")
public abstract class Command {
    protected final TelegramBot receiver;
    protected final Environment environment;

    public abstract void execute(CommandDto commandDto);
}
