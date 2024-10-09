package faang.school.notificationservice.service.telegram.command;

import faang.school.notificationservice.dto.CommandDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@RequiredArgsConstructor
@PropertySource("classpath:messages.properties")
public abstract class Command {

    protected final Environment environment;

    public abstract SendMessage execute(CommandDto commandDto);
}
