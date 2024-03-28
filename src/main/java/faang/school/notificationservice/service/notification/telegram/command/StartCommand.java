package faang.school.notificationservice.service.notification.telegram.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@Slf4j
public class StartCommand extends Command {
    @Value("${telegram.command.start}")
    private String startCommand;
    public StartCommand(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public SendMessage process(long chatId, String text) {
        return buildSendMessage(
                chatId,
                messageSource.getMessage("telegram.start-message", new Object[]{}, this.locale)
        );
    }

    @Override
    public boolean isApplicable(String textCommand) {
        return textCommand.equals(startCommand);
    }
}
