package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.dto.CommandDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Getter
@RequiredArgsConstructor
@PropertySource("classpath:messages.properties")
public abstract class Command {
    protected final Environment environment;

    public abstract SendMessage execute(CommandDto commandDto);
}
